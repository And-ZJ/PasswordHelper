package com.andzj.passwordhelper.bean;

/**
 * Created by zj on 2017/2/3.
 */

public class EncryptionInfo {
    private Integer id;
    private String ac;
    private String an;
    private String nn;
    private String bpn;
    private String bm;

    private String bcc;
    private String bca;
    private String bcp;
    private String bcn;

    private String p;
    private String n;
    private String ut;

    public EncryptionInfo()
    {}

    public EncryptionInfo(AccountPasswordInfo info)
    {
        this(info.getId(),info.getAccountCategory(),info.getAccountName(),info.getNickname(),
                info.getBindPhoneNumber(),info.getBindMailbox(),info.getBindCooperationCategory(),
                info.getBindCooperationAccountName(),info.getBindCooperationPassword(),info.getBindCooperationNote(),
                info.getPassword(),info.getNote(),info.getUpdateTime());
    }

    public EncryptionInfo(Integer id, String ac, String an, String nn, String bpn, String bm, String bcc, String bca, String bcp, String bcn, String p, String n, String ut) {
        this.id = id;
        this.ac = ac;
        this.an = an;
        this.nn = nn;
        this.bpn = bpn;
        this.bm = bm;
        this.bcc = bcc;
        this.bca = bca;
        this.bcp = bcp;
        this.bcn = bcn;
        this.p = p;
        this.n = n;
        this.ut = ut;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getBca() {
        return bca;
    }

    public void setBca(String bca) {
        this.bca = bca;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getBcn() {
        return bcn;
    }

    public void setBcn(String bcn) {
        this.bcn = bcn;
    }

    public String getBcp() {
        return bcp;
    }

    public void setBcp(String bcp) {
        this.bcp = bcp;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }
}
