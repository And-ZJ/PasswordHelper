package com.andzj.passwordhelper.bean;

/**
 * Created by zj on 2017/1/18.
 */

public class AccountPasswordHistoryInfo {
    public static String NOT_SET = "未设置";

    private Integer id;
    private Integer indexId;
    private String accountCategory;
    private String accountName;
    private String nickname;
    private String bindPhoneNumber;
    private String bindMailbox;
    //private String bindCooperationAccount;
    private String bindCooperationCategory;
    private String bindCooperationAccountName;
    private String bindCooperationPassword;
    private String bindCooperationNote;

    private String password;
    private String note;
    private String updateTime;

    public AccountPasswordHistoryInfo(Integer id, Integer indexId,
                                      String accountCategory, String accountName, String nickname,
                                      String bindPhoneNumber, String bindMailbox,
                                      String bindCooperationCategory, String bindCooperationAccountName, String bindCooperationPassword, String bindCooperationNote,
                                      String password, String note, String updateTime) {
        this.id = id;
        this.indexId = indexId;
        this.accountCategory = accountCategory;
        this.accountName = accountName;
        this.nickname = nickname;
        this.bindPhoneNumber = bindPhoneNumber;
        this.bindMailbox = bindMailbox;
        this.bindCooperationCategory = bindCooperationCategory;
        this.bindCooperationAccountName = bindCooperationAccountName;
        this.bindCooperationPassword = bindCooperationPassword;
        this.bindCooperationNote = bindCooperationNote;
        this.password = password;
        this.note = note;
        this.updateTime = updateTime;
    }

    public AccountPasswordHistoryInfo(AccountPasswordInfo info)
    {
        this(0,info.getId(),
                info.getAccountCategory(),info.getAccountName(),info.getNickname(),
                info.getBindPhoneNumber(),info.getBindMailbox(),
                info.getBindCooperationCategory(),info.getBindCooperationAccountName(),info.getBindCooperationPassword(),info.getBindCooperationNote(),
                info.getPassword(),info.getNote(),info.getUpdateTime());
    }

    public AccountPasswordHistoryInfo(){}

    public void dislodgeEmptyString()
    {
        accountCategory = dislodgeEmptyString(accountCategory);
        accountName = dislodgeEmptyString(accountName);
        nickname = dislodgeEmptyString(nickname);
        bindPhoneNumber = dislodgeEmptyString(bindPhoneNumber);
        bindMailbox = dislodgeEmptyString(bindMailbox);
        bindCooperationCategory = dislodgeEmptyString(bindCooperationCategory);
        bindCooperationAccountName = dislodgeEmptyString(bindCooperationAccountName);
        bindCooperationPassword = dislodgeEmptyString(bindCooperationPassword);
        bindCooperationNote = dislodgeEmptyString(bindCooperationNote);
        password = dislodgeEmptyString(password);
        note = dislodgeEmptyString(note);
        updateTime = dislodgeEmptyString(updateTime);
    }

    private String dislodgeEmptyString(String s)
    {
        return (s == null || "".equals(s)) ? null : s;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBindPhoneNumber() {
        return bindPhoneNumber;
    }

    public void setBindPhoneNumber(String bindPhoneNumber) {
        this.bindPhoneNumber = bindPhoneNumber;
    }

    public String getBindMailbox() {
        return bindMailbox;
    }

    public void setBindMailbox(String bindMailbox) {
        this.bindMailbox = bindMailbox;
    }

    public String getBindCooperationPassword() {
        return bindCooperationPassword;
    }

    public void setBindCooperationPassword(String bindCooperationPassword) {
        this.bindCooperationPassword = bindCooperationPassword;
    }

    public String getBindCooperationNote() {
        return bindCooperationNote;
    }

    public void setBindCooperationNote(String bindCooperationNote) {
        this.bindCooperationNote = bindCooperationNote;
    }

    public String getBindCooperationCategory() {
        return bindCooperationCategory;
    }

    public void setBindCooperationCategory(String bindCooperationCategory) {
        this.bindCooperationCategory = bindCooperationCategory;
    }

    public String getBindCooperationAccountName() {
        return bindCooperationAccountName;
    }

    public void setBindCooperationAccountName(String bindCooperationAccountName) {
        this.bindCooperationAccountName = bindCooperationAccountName;
    }

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
