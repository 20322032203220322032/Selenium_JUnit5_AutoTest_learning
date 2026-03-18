package cn.tcmeta.logintest.Tools.GetInformation;

public class GetCurrentMethodName {
    /**
     * 获取用例方法名，即用例编号
     * 兼容性：Selenium 4.x+
     * 性能：单次调用0.05-0.1ms
     */
    public static String getCurrentMethodName() {
        /*
         堆栈层级说明：
         0=getStackTrace()，
         1=getCurrentMethodName()，
         2=调用该方法的业务方法（如LoginPage类中的login方法），
         3=调用2的业务方法(如用例001)
         4.5...以此类推
         */
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste[2].getMethodName().matches("TC_LOGIN_...T")) {
            return ste[2].getMethodName();
        }
        for(int i=3;i<ste.length;i++){
            if(ste[i].getMethodName().matches("TC_LOGIN_...T")){
                return ste[i].getMethodName();
            }
        }
        return "未找到当前方法名称";
    }
}
