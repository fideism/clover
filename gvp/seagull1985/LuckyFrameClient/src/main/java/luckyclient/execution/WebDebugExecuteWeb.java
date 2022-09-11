package luckyclient.execution;

import luckyclient.execution.httpinterface.TestControl;
import luckyclient.execution.httpinterface.WebTestCaseDebug;
import luckyclient.utils.LogUtil;
import org.apache.log4j.PropertyConfigurator;
import springboot.RunService;

import java.io.File;

public class WebDebugExecuteWeb extends TestControl {
    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure(RunService.APPLICATION_HOME + File.separator + "log4j.conf");
            String caseIdStr = args[0];
            String userIdStr = args[1];
            //�޸ĵ�
            String caseTypeStr =args[2];
            String browserTypeStr =args[3];
            WebTestCaseDebug.oneCaseDebug(caseIdStr, userIdStr,caseTypeStr,browserTypeStr);
        } catch (Exception e) {
            LogUtil.APP.error("�����������������������쳣�����飡",e);
        } finally{
            System.exit(0);
        }
    }
}
