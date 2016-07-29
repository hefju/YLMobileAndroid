package TaskClass;

/**
 * Created by Administrator on 2016-03-07.
 */
public class GatherPrint {

    public String ClintName ;
    public String SiteName ;
    public String TradeTime;
    public String CarNumber;
    public String HomName;
    public String TaskNumber;



    public String Givemoneyfull;
    public String Givemoneyempty;
    public String Givecardfull;
    public String Givecardempty;
    public String Givevoucherfull;
    public String Givevoucherempty;
    public String Givevoucherbagfull;
    public String Givevoucherbagempty;

    public String Zswsmoneyfull;
    public String Zswsmoneyempty;
    public String Zswscardfull;
    public String Zswscardempty;
    public String Zswsvoucherfull;
    public String Zswsvoucherempty;
    public String Zswsvoucherbagfull;
    public String Zswsvoucherbagempty;

    public String Sxjmoneyfull;
    public String Sxjmoneyempty;
    public String Sxjcardfull;
    public String Sxjcardempty;
    public String Sxjvoucherfull;
    public String Sxjvoucherempty;
    public String Sxjvoucherbagfull;
    public String Sxjvoucherbagempty;

    public String Jkxmoneyfull;
    public String Jkxmoneyempty;
    public String Jkxcardfull;
    public String Jkxcardempty;
    public String Jkxvoucherfull;
    public String Jkxvoucherempty;
    public String Jkxvoucherbagfull;
    public String Jkxvoucherbagempty;

    public String Thdbmoneyfull;
    public String Thdbmoneyempty;
    public String Thdbcardfull;
    public String Thdbcardempty;
    public String Thdbvoucherfull;
    public String Thdbvoucherempty;
    public String Thdbvoucherbagfull;
    public String Thdbvoucherbagempty;

    public String Khdbmoneyfull;
    public String Khdbmoneyempty;
    public String Khdbcardfull;
    public String Khdbcardempty;
    public String Khdbvoucherfull;
    public String Khdbvoucherempty;
    public String Khdbvoucherbagfull;
    public String Khdbvoucherbagempty;

    public String Qysskmoneyfull;
    public String Qysskmoneyempty;
    public String Qysskcardfull;
    public String Qysskcardempty;
    public String Qysskvoucherfull;
    public String Qysskvoucherempty;
    public String Qysskvoucherbagfull;
    public String Qysskvoucherbagempty;

    public String GiveTotal;

    public String GetTotalmoneyfull;
    public String GetTotalcardfull;
    public String GetTotalvoucherfull;
    public String GetTotalvoucherbagfull;
    public String GetTotalmoneyempty;
    public String GetTotalcardempty;
    public String GetTotalvoucherempty;
    public String GetTotalvoucherbagempty;

    public String GetTotalmoney;
    public String GetTotalcard;
    public String GetTotalvoucher;
    public String GetTotalvoucherbag;

    public GatherPrint() {
    }

    public GatherPrint(
                       String givemoneyfull, String givemoneyempty, String givecardfull,
                       String givecardempty, String givevoucherfull, String givevoucherempty,
                       String giveoucherbagfull, String giveoucherbagempty,
                       String zswsmoneyfull, String zswsmoneyempty, String zswscardfull,
                       String zswscardempty, String zswsvoucherfull, String zswsvoucherempty,
                       String zswsvoucherbagfull, String zswsyoucherbagempty, String sxjmoneyfull,
                       String sxjmoneyempty, String sxjcardfull, String sxjcardempty,
                       String sxjvoucherfull, String sxjvoucherempty, String sxjvoucherbagfull,
                       String sxjvoucherbagempty, String jkxmoneyfull, String jkxmoneyempty,
                       String jkxcardfull, String jkxcardempty, String jkxvoucherfull,
                       String jkxvoucherempty, String jkxvoucherbagfull, String jkxvoucherbagempty,
                       String thdbmoneyfull, String thdbmoneyempty, String thdbcardfull,
                       String thdbcardempty, String thdbvoucherfull, String thdbvoucherempty,
                       String thdbvoucherbagfull, String thdbvoucherbagempty,
                       String khdbmoneyfull, String khdbmoneyempty, String khdbcardfull,
                       String khdbcardempty, String khdbvoucherfull, String khdbvoucherempty,
                       String khdbvoucherbagfull, String khdbvoucherbagempty,
                       String qysskmoneyfull, String qysskmoneyempty, String qysskcardfull,
                       String qysskcardempty, String qysskvoucherfull, String qysskvoucherempty,
                       String qysskvoucherbagfull, String qysskvoucherbagempty,
                       String giveTotal, String getTotalmoneyfull, String getTotalcardfull,
                       String getTotalvoucherfull, String getTotalvoucherbagfull,
                       String getTotalmoneyempty, String getTotalcardempty,
                       String getTotalvoucherempty, String getTotalvoucherbagempty,
                       String getTotalmoney, String getTotalcard, String getTotalvoucher,
                       String getTotalvoucherbag) {
        Givemoneyfull = givemoneyfull;
        Givemoneyempty = givemoneyempty;
        Givecardfull = givecardfull;
        Givecardempty = givecardempty;
        Givevoucherfull = givevoucherfull;
        Givevoucherempty = givevoucherempty;
        Givevoucherbagfull = giveoucherbagfull;
        Givevoucherbagempty = giveoucherbagempty;
        Zswsmoneyfull = zswsmoneyfull;
        Zswsmoneyempty = zswsmoneyempty;
        Zswscardfull = zswscardfull;
        Zswscardempty = zswscardempty;
        Zswsvoucherfull = zswsvoucherfull;
        Zswsvoucherempty = zswsvoucherempty;
        Zswsvoucherbagfull = zswsvoucherbagfull;
        Zswsvoucherbagempty = zswsyoucherbagempty;
        Sxjmoneyfull = sxjmoneyfull;
        Sxjmoneyempty = sxjmoneyempty;
        Sxjcardfull = sxjcardfull;
        Sxjcardempty = sxjcardempty;
        Sxjvoucherfull = sxjvoucherfull;
        Sxjvoucherempty = sxjvoucherempty;
        Sxjvoucherbagfull = sxjvoucherbagfull;
        Sxjvoucherbagempty = sxjvoucherbagempty;
        Jkxmoneyfull = jkxmoneyfull;
        Jkxmoneyempty = jkxmoneyempty;
        Jkxcardfull = jkxcardfull;
        Jkxcardempty = jkxcardempty;
        Jkxvoucherfull = jkxvoucherfull;
        Jkxvoucherempty = jkxvoucherempty;
        Jkxvoucherbagfull = jkxvoucherbagfull;
        Jkxvoucherbagempty = jkxvoucherbagempty;
        Thdbmoneyfull = thdbmoneyfull;
        Thdbmoneyempty = thdbmoneyempty;
        Thdbcardfull = thdbcardfull;
        Thdbcardempty = thdbcardempty;
        Thdbvoucherfull = thdbvoucherfull;
        Thdbvoucherempty = thdbvoucherempty;
        Thdbvoucherbagfull = thdbvoucherbagfull;
        Thdbvoucherbagempty = thdbvoucherbagempty;
        Khdbmoneyfull = khdbmoneyfull;
        Khdbmoneyempty = khdbmoneyempty;
        Khdbcardfull = khdbcardfull;
        Khdbcardempty = khdbcardempty;
        Khdbvoucherfull = khdbvoucherfull;
        Khdbvoucherempty = khdbvoucherempty;
        Khdbvoucherbagfull = khdbvoucherbagfull;
        Khdbvoucherbagempty = khdbvoucherbagempty;
        Qysskmoneyfull = qysskmoneyfull;
        Qysskmoneyempty = qysskmoneyempty;
        Qysskcardfull = qysskcardfull;
        Qysskcardempty = qysskcardempty;
        Qysskvoucherfull = qysskvoucherfull;
        Qysskvoucherempty = qysskvoucherempty;
        Qysskvoucherbagfull = qysskvoucherbagfull;
        Qysskvoucherbagempty = qysskvoucherbagempty;
        GiveTotal = giveTotal;
        GetTotalmoneyfull = getTotalmoneyfull;
        GetTotalcardfull = getTotalcardfull;
        GetTotalvoucherfull = getTotalvoucherfull;
        GetTotalvoucherbagfull = getTotalvoucherbagfull;
        GetTotalmoneyempty = getTotalmoneyempty;
        GetTotalcardempty = getTotalcardempty;
        GetTotalvoucherempty = getTotalvoucherempty;
        GetTotalvoucherbagempty = getTotalvoucherbagempty;
        GetTotalmoney = getTotalmoney;
        GetTotalcard = getTotalcard;
        GetTotalvoucher = getTotalvoucher;
        GetTotalvoucherbag = getTotalvoucherbag;
    }

    public String getClintName() {
        return ClintName;
    }

    public void setClintName(String clintName) {
        ClintName = clintName;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getTradeTime() {
        return TradeTime;
    }

    public void setTradeTime(String tradeTime) {
        TradeTime = tradeTime;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getHomName() {
        return HomName;
    }

    public void setHomName(String homName) {
        HomName = homName;
    }

    public String getTaskNumber() {
        return TaskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        TaskNumber = taskNumber;
    }

    public String getGivemoneyfull() {
        return Givemoneyfull;
    }

    public void setGivemoneyfull(String givemoneyfull) {
        Givemoneyfull = givemoneyfull;
    }

    public String getGivemoneyempty() {
        return Givemoneyempty;
    }

    public void setGivemoneyempty(String givemoneyempty) {
        Givemoneyempty = givemoneyempty;
    }

    public String getGivecardfull() {
        return Givecardfull;
    }

    public void setGivecardfull(String givecardfull) {
        Givecardfull = givecardfull;
    }

    public String getGivecardempty() {
        return Givecardempty;
    }

    public void setGivecardempty(String givecardempty) {
        Givecardempty = givecardempty;
    }

    public String getGivevoucherfull() {
        return Givevoucherfull;
    }

    public void setGivevoucherfull(String givevoucherfull) {
        Givevoucherfull = givevoucherfull;
    }

    public String getGivevoucherempty() {
        return Givevoucherempty;
    }

    public void setGivevoucherempty(String givevoucherempty) {
        Givevoucherempty = givevoucherempty;
    }

    public String getGivevoucherbagfull() {
        return Givevoucherbagfull;
    }

    public void setGivevoucherbagfull(String givevoucherbagfull) {
        Givevoucherbagfull = givevoucherbagfull;
    }

    public String getGivevoucherbagempty() {
        return Givevoucherbagempty;
    }

    public void setGivevoucherbagempty(String givevoucherbagempty) {
        Givevoucherbagempty = givevoucherbagempty;
    }

    public String getZswsmoneyfull() {
        return Zswsmoneyfull;
    }

    public void setZswsmoneyfull(String zswsmoneyfull) {
        Zswsmoneyfull = zswsmoneyfull;
    }

    public String getZswsmoneyempty() {
        return Zswsmoneyempty;
    }

    public void setZswsmoneyempty(String zswsmoneyempty) {
        Zswsmoneyempty = zswsmoneyempty;
    }

    public String getZswscardfull() {
        return Zswscardfull;
    }

    public void setZswscardfull(String zswscardfull) {
        Zswscardfull = zswscardfull;
    }

    public String getZswscardempty() {
        return Zswscardempty;
    }

    public void setZswscardempty(String zswscardempty) {
        Zswscardempty = zswscardempty;
    }

    public String getZswsvoucherfull() {
        return Zswsvoucherfull;
    }

    public void setZswsvoucherfull(String zswsvoucherfull) {
        Zswsvoucherfull = zswsvoucherfull;
    }

    public String getZswsvoucherempty() {
        return Zswsvoucherempty;
    }

    public void setZswsvoucherempty(String zswsvoucherempty) {
        Zswsvoucherempty = zswsvoucherempty;
    }

    public String getZswsvoucherbagfull() {
        return Zswsvoucherbagfull;
    }

    public void setZswsvoucherbagfull(String zswsvoucherbagfull) {
        Zswsvoucherbagfull = zswsvoucherbagfull;
    }

    public String getZswsvoucherbagempty() {
        return Zswsvoucherbagempty;
    }

    public void setZswsvoucherbagempty(String zswsvoucherbagempty) {
        Zswsvoucherbagempty = zswsvoucherbagempty;
    }

    public String getSxjmoneyfull() {
        return Sxjmoneyfull;
    }

    public void setSxjmoneyfull(String sxjmoneyfull) {
        Sxjmoneyfull = sxjmoneyfull;
    }

    public String getSxjmoneyempty() {
        return Sxjmoneyempty;
    }

    public void setSxjmoneyempty(String sxjmoneyempty) {
        Sxjmoneyempty = sxjmoneyempty;
    }

    public String getSxjcardfull() {
        return Sxjcardfull;
    }

    public void setSxjcardfull(String sxjcardfull) {
        Sxjcardfull = sxjcardfull;
    }

    public String getSxjcardempty() {
        return Sxjcardempty;
    }

    public void setSxjcardempty(String sxjcardempty) {
        Sxjcardempty = sxjcardempty;
    }

    public String getSxjvoucherfull() {
        return Sxjvoucherfull;
    }

    public void setSxjvoucherfull(String sxjvoucherfull) {
        Sxjvoucherfull = sxjvoucherfull;
    }

    public String getSxjvoucherempty() {
        return Sxjvoucherempty;
    }

    public void setSxjvoucherempty(String sxjvoucherempty) {
        Sxjvoucherempty = sxjvoucherempty;
    }

    public String getSxjvoucherbagfull() {
        return Sxjvoucherbagfull;
    }

    public void setSxjvoucherbagfull(String sxjvoucherbagfull) {
        Sxjvoucherbagfull = sxjvoucherbagfull;
    }

    public String getSxjvoucherbagempty() {
        return Sxjvoucherbagempty;
    }

    public void setSxjvoucherbagempty(String sxjvoucherbagempty) {
        Sxjvoucherbagempty = sxjvoucherbagempty;
    }

    public String getJkxmoneyfull() {
        return Jkxmoneyfull;
    }

    public void setJkxmoneyfull(String jkxmoneyfull) {
        Jkxmoneyfull = jkxmoneyfull;
    }

    public String getJkxmoneyempty() {
        return Jkxmoneyempty;
    }

    public void setJkxmoneyempty(String jkxmoneyempty) {
        Jkxmoneyempty = jkxmoneyempty;
    }

    public String getJkxcardfull() {
        return Jkxcardfull;
    }

    public void setJkxcardfull(String jkxcardfull) {
        Jkxcardfull = jkxcardfull;
    }

    public String getJkxcardempty() {
        return Jkxcardempty;
    }

    public void setJkxcardempty(String jkxcardempty) {
        Jkxcardempty = jkxcardempty;
    }

    public String getJkxvoucherfull() {
        return Jkxvoucherfull;
    }

    public void setJkxvoucherfull(String jkxvoucherfull) {
        Jkxvoucherfull = jkxvoucherfull;
    }

    public String getJkxvoucherempty() {
        return Jkxvoucherempty;
    }

    public void setJkxvoucherempty(String jkxvoucherempty) {
        Jkxvoucherempty = jkxvoucherempty;
    }

    public String getJkxvoucherbagfull() {
        return Jkxvoucherbagfull;
    }

    public void setJkxvoucherbagfull(String jkxvoucherbagfull) {
        Jkxvoucherbagfull = jkxvoucherbagfull;
    }

    public String getJkxvoucherbagempty() {
        return Jkxvoucherbagempty;
    }

    public void setJkxvoucherbagempty(String jkxvoucherbagempty) {
        Jkxvoucherbagempty = jkxvoucherbagempty;
    }

    public String getThdbmoneyfull() {
        return Thdbmoneyfull;
    }

    public void setThdbmoneyfull(String thdbmoneyfull) {
        Thdbmoneyfull = thdbmoneyfull;
    }

    public String getThdbmoneyempty() {
        return Thdbmoneyempty;
    }

    public void setThdbmoneyempty(String thdbmoneyempty) {
        Thdbmoneyempty = thdbmoneyempty;
    }

    public String getThdbcardfull() {
        return Thdbcardfull;
    }

    public void setThdbcardfull(String thdbcardfull) {
        Thdbcardfull = thdbcardfull;
    }

    public String getThdbcardempty() {
        return Thdbcardempty;
    }

    public void setThdbcardempty(String thdbcardempty) {
        Thdbcardempty = thdbcardempty;
    }

    public String getThdbvoucherfull() {
        return Thdbvoucherfull;
    }

    public void setThdbvoucherfull(String thdbvoucherfull) {
        Thdbvoucherfull = thdbvoucherfull;
    }

    public String getThdbvoucherempty() {
        return Thdbvoucherempty;
    }

    public void setThdbvoucherempty(String thdbvoucherempty) {
        Thdbvoucherempty = thdbvoucherempty;
    }

    public String getThdbvoucherbagfull() {
        return Thdbvoucherbagfull;
    }

    public void setThdbvoucherbagfull(String thdbvoucherbagfull) {
        Thdbvoucherbagfull = thdbvoucherbagfull;
    }

    public String getThdbvoucherbagempty() {
        return Thdbvoucherbagempty;
    }

    public void setThdbvoucherbagempty(String thdbvoucherbagempty) {
        Thdbvoucherbagempty = thdbvoucherbagempty;
    }

    public String getKhdbmoneyfull() {
        return Khdbmoneyfull;
    }

    public void setKhdbmoneyfull(String khdbmoneyfull) {
        Khdbmoneyfull = khdbmoneyfull;
    }

    public String getKhdbmoneyempty() {
        return Khdbmoneyempty;
    }

    public void setKhdbmoneyempty(String khdbmoneyempty) {
        Khdbmoneyempty = khdbmoneyempty;
    }

    public String getKhdbcardfull() {
        return Khdbcardfull;
    }

    public void setKhdbcardfull(String khdbcardfull) {
        Khdbcardfull = khdbcardfull;
    }

    public String getKhdbcardempty() {
        return Khdbcardempty;
    }

    public void setKhdbcardempty(String khdbcardempty) {
        Khdbcardempty = khdbcardempty;
    }

    public String getKhdbvoucherfull() {
        return Khdbvoucherfull;
    }

    public void setKhdbvoucherfull(String khdbvoucherfull) {
        Khdbvoucherfull = khdbvoucherfull;
    }

    public String getKhdbvoucherempty() {
        return Khdbvoucherempty;
    }

    public void setKhdbvoucherempty(String khdbvoucherempty) {
        Khdbvoucherempty = khdbvoucherempty;
    }

    public String getKhdbvoucherbagfull() {
        return Khdbvoucherbagfull;
    }

    public void setKhdbvoucherbagfull(String khdbvoucherbagfull) {
        Khdbvoucherbagfull = khdbvoucherbagfull;
    }

    public String getKhdbvoucherbagempty() {
        return Khdbvoucherbagempty;
    }

    public void setKhdbvoucherbagempty(String khdbvoucherbagempty) {
        Khdbvoucherbagempty = khdbvoucherbagempty;
    }

    public String getQysskmoneyfull() {
        return Qysskmoneyfull;
    }

    public void setQysskmoneyfull(String qysskmoneyfull) {
        Qysskmoneyfull = qysskmoneyfull;
    }

    public String getQysskmoneyempty() {
        return Qysskmoneyempty;
    }

    public void setQysskmoneyempty(String qysskmoneyempty) {
        Qysskmoneyempty = qysskmoneyempty;
    }

    public String getQysskcardfull() {
        return Qysskcardfull;
    }

    public void setQysskcardfull(String qysskcardfull) {
        Qysskcardfull = qysskcardfull;
    }

    public String getQysskcardempty() {
        return Qysskcardempty;
    }

    public void setQysskcardempty(String qysskcardempty) {
        Qysskcardempty = qysskcardempty;
    }

    public String getQysskvoucherfull() {
        return Qysskvoucherfull;
    }

    public void setQysskvoucherfull(String qysskvoucherfull) {
        Qysskvoucherfull = qysskvoucherfull;
    }

    public String getQysskvoucherempty() {
        return Qysskvoucherempty;
    }

    public void setQysskvoucherempty(String qysskvoucherempty) {
        Qysskvoucherempty = qysskvoucherempty;
    }

    public String getQysskvoucherbagfull() {
        return Qysskvoucherbagfull;
    }

    public void setQysskvoucherbagfull(String qysskvoucherbagfull) {
        Qysskvoucherbagfull = qysskvoucherbagfull;
    }

    public String getQysskvoucherbagempty() {
        return Qysskvoucherbagempty;
    }

    public void setQysskvoucherbagempty(String qysskvoucherbagempty) {
        Qysskvoucherbagempty = qysskvoucherbagempty;
    }

    public String getGiveTotal() {
        return GiveTotal;
    }

    public void setGiveTotal(String giveTotal) {
        GiveTotal = giveTotal;
    }

    public String getGetTotalmoneyfull() {
        return GetTotalmoneyfull;
    }

    public void setGetTotalmoneyfull(String getTotalmoneyfull) {
        GetTotalmoneyfull = getTotalmoneyfull;
    }

    public String getGetTotalcardfull() {
        return GetTotalcardfull;
    }

    public void setGetTotalcardfull(String getTotalcardfull) {
        GetTotalcardfull = getTotalcardfull;
    }

    public String getGetTotalvoucherfull() {
        return GetTotalvoucherfull;
    }

    public void setGetTotalvoucherfull(String getTotalvoucherfull) {
        GetTotalvoucherfull = getTotalvoucherfull;
    }

    public String getGetTotalvoucherbagfull() {
        return GetTotalvoucherbagfull;
    }

    public void setGetTotalvoucherbagfull(String getTotalvoucherbagfull) {
        GetTotalvoucherbagfull = getTotalvoucherbagfull;
    }

    public String getGetTotalmoneyempty() {
        return GetTotalmoneyempty;
    }

    public void setGetTotalmoneyempty(String getTotalmoneyempty) {
        GetTotalmoneyempty = getTotalmoneyempty;
    }

    public String getGetTotalcardempty() {
        return GetTotalcardempty;
    }

    public void setGetTotalcardempty(String getTotalcardempty) {
        GetTotalcardempty = getTotalcardempty;
    }

    public String getGetTotalvoucherempty() {
        return GetTotalvoucherempty;
    }

    public void setGetTotalvoucherempty(String getTotalvoucherempty) {
        GetTotalvoucherempty = getTotalvoucherempty;
    }

    public String getGetTotalvoucherbagempty() {
        return GetTotalvoucherbagempty;
    }

    public void setGetTotalvoucherbagempty(String getTotalvoucherbagempty) {
        GetTotalvoucherbagempty = getTotalvoucherbagempty;
    }

    public String getGetTotalmoney() {
        return GetTotalmoney;
    }

    public void setGetTotalmoney(String getTotalmoney) {
        GetTotalmoney = getTotalmoney;
    }

    public String getGetTotalcard() {
        return GetTotalcard;
    }

    public void setGetTotalcard(String getTotalcard) {
        GetTotalcard = getTotalcard;
    }

    public String getGetTotalvoucher() {
        return GetTotalvoucher;
    }

    public void setGetTotalvoucher(String getTotalvoucher) {
        GetTotalvoucher = getTotalvoucher;
    }

    public String getGetTotalvoucherbag() {
        return GetTotalvoucherbag;
    }

    public void setGetTotalvoucherbag(String getTotalvoucherbag) {
        GetTotalvoucherbag = getTotalvoucherbag;
    }

    @Override
    public String toString() {
        return "GatherPrint{" +
                "ClintName='" + ClintName + '\'' +
                ", SiteName='" + SiteName + '\'' +
                ", TradeTime='" + TradeTime + '\'' +
                ", CarNumber='" + CarNumber + '\'' +
                ", HomName='" + HomName + '\'' +
                ", TaskNumber='" + TaskNumber + '\'' +
                ", Givemoneyfull='" + Givemoneyfull + '\'' +
                ", Givemoneyempty='" + Givemoneyempty + '\'' +
                ", Givecardfull='" + Givecardfull + '\'' +
                ", Givecardempty='" + Givecardempty + '\'' +
                ", Givevoucherfull='" + Givevoucherfull + '\'' +
                ", Givevoucherempty='" + Givevoucherempty + '\'' +
                ", Givevoucherbagfull='" + Givevoucherbagfull + '\'' +
                ", Givevoucherbagempty='" + Givevoucherbagempty + '\'' +
                ", Zswsmoneyfull='" + Zswsmoneyfull + '\'' +
                ", Zswsmoneyempty='" + Zswsmoneyempty + '\'' +
                ", Zswscardfull='" + Zswscardfull + '\'' +
                ", Zswscardempty='" + Zswscardempty + '\'' +
                ", Zswsvoucherfull='" + Zswsvoucherfull + '\'' +
                ", Zswsvoucherempty='" + Zswsvoucherempty + '\'' +
                ", Zswsvoucherbagfull='" + Zswsvoucherbagfull + '\'' +
                ", Zswsvoucherbagempty='" + Zswsvoucherbagempty + '\'' +
                ", Sxjmoneyfull='" + Sxjmoneyfull + '\'' +
                ", Sxjmoneyempty='" + Sxjmoneyempty + '\'' +
                ", Sxjcardfull='" + Sxjcardfull + '\'' +
                ", Sxjcardempty='" + Sxjcardempty + '\'' +
                ", Sxjvoucherfull='" + Sxjvoucherfull + '\'' +
                ", Sxjvoucherempty='" + Sxjvoucherempty + '\'' +
                ", Sxjvoucherbagfull='" + Sxjvoucherbagfull + '\'' +
                ", Sxjvoucherbagempty='" + Sxjvoucherbagempty + '\'' +
                ", Jkxmoneyfull='" + Jkxmoneyfull + '\'' +
                ", Jkxmoneyempty='" + Jkxmoneyempty + '\'' +
                ", Jkxcardfull='" + Jkxcardfull + '\'' +
                ", Jkxcardempty='" + Jkxcardempty + '\'' +
                ", Jkxvoucherfull='" + Jkxvoucherfull + '\'' +
                ", Jkxvoucherempty='" + Jkxvoucherempty + '\'' +
                ", Jkxvoucherbagfull='" + Jkxvoucherbagfull + '\'' +
                ", Jkxvoucherbagempty='" + Jkxvoucherbagempty + '\'' +
                ", Thdbmoneyfull='" + Thdbmoneyfull + '\'' +
                ", Thdbmoneyempty='" + Thdbmoneyempty + '\'' +
                ", Thdbcardfull='" + Thdbcardfull + '\'' +
                ", Thdbcardempty='" + Thdbcardempty + '\'' +
                ", Thdbvoucherfull='" + Thdbvoucherfull + '\'' +
                ", Thdbvoucherempty='" + Thdbvoucherempty + '\'' +
                ", Thdbvoucherbagfull='" + Thdbvoucherbagfull + '\'' +
                ", Thdbvoucherbagempty='" + Thdbvoucherbagempty + '\'' +
                ", Khdbmoneyfull='" + Khdbmoneyfull + '\'' +
                ", Khdbmoneyempty='" + Khdbmoneyempty + '\'' +
                ", Khdbcardfull='" + Khdbcardfull + '\'' +
                ", Khdbcardempty='" + Khdbcardempty + '\'' +
                ", Khdbvoucherfull='" + Khdbvoucherfull + '\'' +
                ", Khdbvoucherempty='" + Khdbvoucherempty + '\'' +
                ", Khdbvoucherbagfull='" + Khdbvoucherbagfull + '\'' +
                ", Khdbvoucherbagempty='" + Khdbvoucherbagempty + '\'' +
                ", Qysskmoneyfull='" + Qysskmoneyfull + '\'' +
                ", Qysskmoneyempty='" + Qysskmoneyempty + '\'' +
                ", Qysskcardfull='" + Qysskcardfull + '\'' +
                ", Qysskcardempty='" + Qysskcardempty + '\'' +
                ", Qysskvoucherfull='" + Qysskvoucherfull + '\'' +
                ", Qysskvoucherempty='" + Qysskvoucherempty + '\'' +
                ", Qysskvoucherbagfull='" + Qysskvoucherbagfull + '\'' +
                ", Qysskvoucherbagempty='" + Qysskvoucherbagempty + '\'' +
                ", GiveTotal='" + GiveTotal + '\'' +
                ", GetTotalmoneyfull='" + GetTotalmoneyfull + '\'' +
                ", GetTotalcardfull='" + GetTotalcardfull + '\'' +
                ", GetTotalvoucherfull='" + GetTotalvoucherfull + '\'' +
                ", GetTotalvoucherbagfull='" + GetTotalvoucherbagfull + '\'' +
                ", GetTotalmoneyempty='" + GetTotalmoneyempty + '\'' +
                ", GetTotalcardempty='" + GetTotalcardempty + '\'' +
                ", GetTotalvoucherempty='" + GetTotalvoucherempty + '\'' +
                ", GetTotalvoucherbagempty='" + GetTotalvoucherbagempty + '\'' +
                ", GetTotalmoney='" + GetTotalmoney + '\'' +
                ", GetTotalcard='" + GetTotalcard + '\'' +
                ", GetTotalvoucher='" + GetTotalvoucher + '\'' +
                ", GetTotalvoucherbag='" + GetTotalvoucherbag + '\'' +
                '}';
    }
}
