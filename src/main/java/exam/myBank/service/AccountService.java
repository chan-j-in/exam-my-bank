package exam.myBank.service;

import exam.myBank.domain.dto.accountDto.*;
import exam.myBank.domain.entity.Account;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.entity.Transaction;
import exam.myBank.domain.repository.AccountRepository;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.domain.repository.TransactionRepository;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
import exam.myBank.type.Bank;
import exam.myBank.type.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@PreAuthorize("isAuthenticated()")
public class AccountService extends BaseService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(MemberRepository memberRepository,
                          AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        super(memberRepository);
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<AccountResponseDto> findAll() {

        Member member = getCurrentMember();
        return toResponseDto(accountRepository.findByMemberId(member.getId()));
    }

    @Transactional
    public ResponseEntity<String> create(AccountCreateRequestDto requestDto) {

        String accountName = requestDto.getAccountName();
        Bank bank = requestDto.getBankEnum();

        if (accountRepository.existsByAccountName(accountName)) throw new IllegalArgumentException("중복된 계좌 이름입니다.");

        Member member = getCurrentMember();

        Account account = Account.builder()
                .accountName(accountName)
                .accountNum(makeAccountNum(bank))
                .member(member)
                .bank(bank)
                .build();
        accountRepository.save(account);

        return ResponseEntity.ok().body("계좌 생성 - " + account.getBank().toString() + " " + account.getAccountNum());
    }

    @Transactional
    public ResponseEntity<String> deposit(String accountNum, TransactionRequestDto requestDto) {

        if (requestDto.getAmount() <=0 ) throw new AppException(ErrorCode.INVALID_AMOUNT, "입금 금액은 0보다 커야 합니다.");

        Long amount = requestDto.getAmount();

        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        Long currentAmount = account.deposit(amount);

        Transaction transaction = createTransaction(account, amount, currentAmount, TransactionType.DEPOSIT, null);
        transactionRepository.save(transaction);
        account.addTransaction(transaction);

        return ResponseEntity.ok().body("입금 완료 - 현재 잔액 : " + currentAmount);
    }

    @Transactional
    public ResponseEntity<String> withdraw(String accountNum, TransactionRequestDto requestDto) {

        Long amount = requestDto.getAmount();

        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        validateSufficientBalance(account, amount);
        Long currentAmount = account.withdraw(amount);

        Transaction transaction = createTransaction(account, amount, currentAmount, TransactionType.WITHDRAW, null);
        transactionRepository.save(transaction);
        account.addTransaction(transaction);

        return ResponseEntity.ok().body("출금 완료 - 현재 잔액 : " + currentAmount);
    }

    @Transactional
    public ResponseEntity<String> transfer(TransferRequestDto requestDto) {

        String myAccountNum = requestDto.getMyAccountNum();
        String targetAccountNum = requestDto.getTargetAccountNum();

        if (myAccountNum.equals(targetAccountNum))
            throw new AppException(ErrorCode.SAME_ACCOUNT_TRANSFER, "같은 계좌로 이체할 수 없습니다.");

        Long amount = requestDto.getAmount();

        Account myAccount = getAccountIfOwnedByCurrentUser(myAccountNum);
        Account targetAccount = accountRepository.findByAccountNum(targetAccountNum)
                        .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "이체 대상 계좌를 찾을 수 없습니다."));

        validateSufficientBalance(myAccount, amount);
        Long myCurrentAmount = myAccount.withdraw(amount);
        Long targetCurrentAmount = targetAccount.deposit(amount);

        Transaction myTransaction = createTransaction(myAccount, amount, myCurrentAmount, TransactionType.TRANSFER, targetAccountNum);
        transactionRepository.save(myTransaction);
        myAccount.addTransaction(myTransaction);

        Transaction targetTransaction = createTransaction(targetAccount, amount, targetCurrentAmount, TransactionType.TRANSFER, myAccountNum);
        transactionRepository.save(targetTransaction);
        targetAccount.addTransaction(targetTransaction);

        return ResponseEntity.ok().body("이체 완료 - 내 계좌  현재 잔액 : " + myCurrentAmount);
    }

    public List<TransactionResponseDto> getTransactionHistory(String accountNum) {

        getAccountIfOwnedByCurrentUser(accountNum);
        Account account = accountRepository.findByAccountNum(accountNum)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "계좌를 찾지 못하였습니다."));

        return transactionRepository.findByAccount(account).stream()
                .map(transcation -> new TransactionResponseDto(
                        transcation.getTransactionType(),
                        transcation.getAmount(),
                        transcation.getBalanceAfterTransaction(),
                        transcation.getTransactionDate(),
                        transcation.getTargetAccountNum()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<String> updateAccountName(UpdateAccountNameRequestDto requestDto) {

        String accountNum = requestDto.getAccountNum();
        String accountName = requestDto.getAccountName();
        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        account.updateAccountName(accountName);

        return ResponseEntity.ok().body("계좌 이름 변경 완료 - " + accountName);
    }

    @Transactional
    public void delete(String accountNum) {

        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        accountRepository.delete(account);
    }

    @Transactional
    public void clear() {
        accountRepository.deleteAll();
    }

    private String makeAccountNum(Bank bank) {

        String bankNum = switch (bank) {
            case A_BANK -> "123";
            case B_BANK -> "456";
            case C_BANK -> "789";
            default -> "000";
        };

        // 8자리 랜덤 숫자 생성
        String randomNum = String.format("%08d", (int) (Math.random() * 1_00_000_000));
        String accountNum = bankNum + randomNum;

        // 중복 체크
        while (accountRepository.findByAccountNum(accountNum).isPresent()) {
            randomNum = String.format("%08d", (int) (Math.random() * 1_00_000_000));
            accountNum = bankNum + randomNum;
        }

        return accountNum;
    }

    private Account getAccountIfOwnedByCurrentUser(String accountNum) {
        Account account = accountRepository.findByAccountNum(accountNum)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "계좌를 찾을 수 없습니다."));

        Member currentMember = getCurrentMember();
        if (!account.getMember().getId().equals(currentMember.getId())) {
            throw new AppException(ErrorCode.USER_NOT_AUTHENTICATED, "해당 계좌에 대한 접근 권한이 없습니다.");
        }

        return account;
    }

    private static Transaction createTransaction(Account account, Long amount, Long currentAmount, TransactionType type, String targetAccountNum) {

        return Transaction.builder()
                .account(account)
                .amount(amount)
                .balanceAfterTransaction(currentAmount)
                .transactionType(type)
                .targetAccountNum(targetAccountNum)
                .build();
    }


    private static List<AccountResponseDto> toResponseDto(List<Account> accounts) {
        return accounts.stream()
                .map(account -> new AccountResponseDto(
                        account.getAccountName(),
                        account.getBank(),
                        account.getAccountNum(),
                        account.getAmount(),
                        account.getCreatedAt(),
                        account.getLastUsedAt()
                ))
                .collect(Collectors.toList());
    }

    private static void validateSufficientBalance(Account account, Long amount) {
        if (account.getAmount() < amount) throw new AppException(ErrorCode.INSUFFICIENT_BALANCE, "잔액이 부족합니다. 현재 잔액 : " + account.getAmount());
    }
}
