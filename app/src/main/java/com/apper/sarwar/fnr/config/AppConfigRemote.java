package com.apper.sarwar.fnr.config;

public class AppConfigRemote {

    public String ACCESS_TOKEN = "";

    /*public String CLIENT_ID = "JDdenON79lYrDpy7CK1FBGGDUfw8MdvDimWENIsO";
    public String CLIENT_SECRET = "CYF7gNXf89x1mEL1nRPG6Nmj2t7RvBhqfOn3IRdlU9wxH5QNnM1BKzGjxjSIZRDwzZTVwnDzZN3rBVUuKJfBhRiE9wXzqXALEGfPMPbXa7IS7mQSWXGPKwPvTMyffdYQ";
    public String BASE_URL = "http://58.84.34.65:8006";*/


    /*public String CLIENT_ID = "60KmdE9mgSDDlr71ubNGP6LWSFyyr9jmTXcIzwFr";
    public String CLIENT_SECRET = "FB4yBoNsvUcotuuzmTe3opPgoxVIpeuXO5F85DOizU0rOsYgqwn2EbIW9xN3r3mJtpy2fxhCTVwmhN1H8pQ3UnzQNbpB7mKDcu1OAQTltHwywWCnmCqpzXR8n1KtQslf";
    public String BASE_URL = "http://www.frprojektman.de";*/


    public String CLIENT_ID = "p0II4kNMelpPqFooR4Kq8KPB7kQg5UmCcFGrrdX2";
    public String CLIENT_SECRET = "sf0StOUHolyCjKa0QlYO277treMsNdYivxpV7NUplsn56v3qvFRot1EYryLvIiJz6rVIEoy2lcPi5NWi94oen4NMpofM8vRzihRnmyNtBR4cxS0nrGMPrVQxq0qwdNJF";
    //public String BASE_URL = "http://18.197.139.76:8006";
    public String BASE_URL = "http://www.frprojektman.de";

    public String GRANT_TYPE = "password";

    public String GRANT_TYPE_REFRESH = "refresh_token";


    public AppConfigRemote(String ACCESS_TOKEN, String CLIENT_ID, String CLIENT_SECRET, String BASE_URL, String GRANT_TYPE) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.BASE_URL = BASE_URL;
        this.GRANT_TYPE = GRANT_TYPE;
    }

    public AppConfigRemote() {
    }

    public String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }

    public void setACCESS_TOKEN(String ACCESS_TOKEN) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
    }

    public String getGRANT_TYPE_REFRESH() {
        return GRANT_TYPE_REFRESH;
    }

    public void setGRANT_TYPE_REFRESH(String GRANT_TYPE_REFRESH) {
        this.GRANT_TYPE_REFRESH = GRANT_TYPE_REFRESH;
    }


    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }

    public void setCLIENT_SECRET(String CLIENT_SECRET) {
        this.CLIENT_SECRET = CLIENT_SECRET;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public String getGRANT_TYPE() {
        return GRANT_TYPE;
    }

    public void setGRANT_TYPE(String GRANT_TYPE) {
        this.GRANT_TYPE = GRANT_TYPE;
    }
}
