package luckyclient.execution.appium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.offbytwo.jenkins.model.BuildResult;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import luckyclient.execution.appium.androidex.AndroidCaseExecution;
import luckyclient.execution.appium.iosex.IosCaseExecution;
import luckyclient.execution.httpinterface.TestControl;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.*;
import luckyclient.tool.jenkins.BuildingInitialization;
import luckyclient.tool.mail.HtmlMail;
import luckyclient.tool.mail.MailSendInitialization;
import luckyclient.tool.shell.RestartServerInitialization;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.AppiumConfig;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * 
 * @date 2017��12��1�� ����9:29:40
 * 
 */
public class AppTestControl {

	/**
	 * ����̨ģʽ���ȼƻ�ִ������
	 * @param planname ���Լƻ�����
	 */
	public static void manualExecutionPlan(String planname) {
		// ������־�����ݿ�
		serverOperation.exetype = 1;
		String taskid = "888888";
		AndroidDriver<AndroidElement> androiddriver = null;
		IOSDriver<IOSElement> iosdriver = null;
		Properties properties = AppiumConfig.getConfiguration();
		try {
			if ("Android".equals(properties.getProperty("platformName"))) {
				androiddriver = AppiumInitialization.setAndroidAppium(properties);
			} else if ("IOS".equals(properties.getProperty("platformName"))) {
				iosdriver = AppiumInitialization.setIosAppium(properties);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("����̨ģʽ��ʼ��Appium Driver�쳣��", e);
		}
		serverOperation caselog = new serverOperation();
		List<ProjectCase> testCases = GetServerApi.getCasesbyplanname(planname);
		List<ProjectCaseParams> pcplist = new ArrayList<>();
		if (testCases.size() != 0) {
			pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testCases.get(0).getProjectId()));
		}
		LogUtil.APP.info("��ǰ�ƻ��ж�ȡ��������{}��",testCases.size());
		int i = 0;
		for (ProjectCase testcase : testCases) {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			if (steps.size() == 0) {
				continue;
			}
			i++;
			LogUtil.APP.info("��ʼִ�мƻ��еĵ�{}����������{}��......",i,testcase.getCaseSign());
			try {
				if ("Android".equals(properties.getProperty("platformName"))) {
					AndroidCaseExecution.caseExcution(testcase, steps, taskid,null, androiddriver, caselog, pcplist);
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					IosCaseExecution.caseExcution(testcase, steps, taskid, null,iosdriver, caselog, pcplist);
				}
			} catch (Exception e) {
				LogUtil.APP.error("�û�ִ�й������׳�Exception�쳣��", e);
			}
			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		}
		LogUtil.APP.info("��ǰ��Ŀ���Լƻ��е������Ѿ�ȫ��ִ�����...");
		// �ر�APP�Լ�appium�Ự
		if ("Android".equals(properties.getProperty("platformName"))) {
			assert androiddriver != null;
			androiddriver.closeApp();
		} else if ("IOS".equals(properties.getProperty("platformName"))) {
			assert iosdriver != null;
			iosdriver.closeApp();
		}
	}

	public static void taskExecutionPlan(TaskExecute task) throws InterruptedException {
		// ��¼��־�����ݿ�
		String taskId=task.getTaskId().toString();
		serverOperation.exetype = 0;
		TestControl.TASKID = taskId;
		AndroidDriver<AndroidElement> androiddriver = null;
		IOSDriver<IOSElement> iosdriver = null;
		Properties properties = AppiumConfig.getConfiguration();
		AppiumService as=null;
		//���������Զ�����Appiume����
		if(Boolean.parseBoolean(properties.getProperty("autoRunAppiumService"))){
			as =new AppiumService();
			as.start();
			Thread.sleep(10000);
		}
		TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId());
		String restartstatus = RestartServerInitialization.restartServerRun(taskId);
		BuildResult buildResult = BuildingInitialization.buildingRun(taskId);
		List<ProjectCaseParams> pcplist = GetServerApi
				.cgetParamsByProjectid(task.getProjectId().toString());
		String projectname = task.getProject().getProjectName();
		String jobname = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId()).getSchedulingName();
        int[] tastcount;
		// �ж��Ƿ�Ҫ�Զ�����TOMCAT
		if (restartstatus.contains("Status:true")) {
			// �ж��Ƿ񹹽��Ƿ�ɹ�
			if (BuildResult.SUCCESS.equals(buildResult)) {

				List<ProjectPlan> plans=new ArrayList<>();
				// ���ƻ�ִ��
				if(taskScheduling.getPlanType()==1){
					ProjectPlan projectPlan=new ProjectPlan();
					projectPlan.setPlanId(taskScheduling.getPlanId());
					plans.add(projectPlan);
				}
				// �ۺ϶�ƻ�ִ��
				else if(taskScheduling.getPlanType()==2){
					plans.addAll(GetServerApi.getPlansbysuiteId(taskScheduling.getSuiteId()));
				}
				LogUtil.APP.info("��ǰ�������� {} �й��С�{}�������Լƻ�...",task.getTaskName(),plans.size());

				int caseCount=0;
				for(ProjectPlan pp:plans){
					List<ProjectCase> cases = GetServerApi.getCasesbyplanId(pp.getPlanId());
					caseCount+=cases.size();
				}

				for(ProjectPlan pp:plans){

					try {
						if ("Android".equals(properties.getProperty("platformName"))) {
							androiddriver = AppiumInitialization.setAndroidAppium(properties);
							LogUtil.APP.info("���AndroidDriver��ʼ������...APPIUM Server��http://{}/wd/hub��",properties.getProperty("appiumsever"));
						} else if ("IOS".equals(properties.getProperty("platformName"))) {
							iosdriver = AppiumInitialization.setIosAppium(properties);
							LogUtil.APP.info("���IOSDriver��ʼ������...APPIUM Server��http://{}/wd/hub��",properties.getProperty("appiumsever"));
						}
					} catch (Exception e) {
						LogUtil.APP.error("��ʼ��AppiumDriver���� ��APPIUM Server��http://{}/wd/hub��",properties.getProperty("appiumsever"), e);
					}
					serverOperation caselog = new serverOperation();
					List<ProjectCase> cases = GetServerApi.getCasesbyplanId(pp.getPlanId());
					LogUtil.APP.info("��ǰ�ƻ���{}���й��С�{}��������������...",pp.getPlanName(),cases.size());
					serverOperation.updateTaskExecuteStatusIng(taskId, caseCount);
					int i = 0;
					for (ProjectCase testcase : cases) {
						i++;
						LogUtil.APP.info("��ʼִ�е�ǰ�������� {} �ĵڡ�{}������������:��{}��......",task.getTaskName(),i,testcase.getCaseSign());
						List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
						if (steps.size() == 0) {
							continue;
						}
						try {
							//���뿪ʼִ�е�����
							caselog.insertTaskCaseExecute(taskId, taskScheduling.getProjectId(),pp.getPlanId(),testcase.getCaseId(),testcase.getCaseSign(), testcase.getCaseName(), 4);
							if ("Android".equals(properties.getProperty("platformName"))) {
								AndroidCaseExecution.caseExcution(testcase, steps, taskId, pp.getPlanId(),androiddriver, caselog, pcplist);
							} else if ("IOS".equals(properties.getProperty("platformName"))) {
								IosCaseExecution.caseExcution(testcase, steps, taskId, pp.getPlanId(),iosdriver, caselog, pcplist);
							}
						} catch (Exception e) {
							LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
						}
						LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
					}
					LogUtil.APP.info("��ǰ��{}�����Լƻ��е������Ѿ�ȫ��ִ�����...",pp.getPlanName());
					// �ر�APP�Լ�appium�Ự
					if ("Android".equals(properties.getProperty("platformName"))) {
						assert androiddriver != null;
						androiddriver.closeApp();
					} else if ("IOS".equals(properties.getProperty("platformName"))) {
						assert iosdriver != null;
						iosdriver.closeApp();
					}
				}
				tastcount = serverOperation.updateTaskExecuteData(taskId, caseCount,2);
				tastcount[0]=caseCount;
				String testtime = serverOperation.getTestTime(taskId);
				LogUtil.APP.info("��ǰ��Ŀ��{}�����Լƻ��е������Ѿ�ȫ��ִ�����...",projectname);
				MailSendInitialization.sendMailInitialization(HtmlMail.htmlSubjectFormat(jobname),
						HtmlMail.htmlContentFormat(tastcount, taskId, buildResult.toString(), restartstatus, testtime, jobname),
						taskId, taskScheduling, tastcount,testtime,buildResult.toString(),restartstatus);

			} else {
				LogUtil.APP.warn("��Ŀ����ʧ�ܣ��Զ��������Զ��˳�����ǰ��JENKINS�м����Ŀ���������");
				MailSendInitialization.sendMailInitialization(jobname, "������Ŀ������ʧ�ܣ��Զ��������Զ��˳�����ǰȥJENKINS�鿴���������", taskId, taskScheduling, null,"0Сʱ0��0��",buildResult.toString(),restartstatus);
			}
		} else {
			LogUtil.APP.warn("��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������");
			MailSendInitialization.sendMailInitialization(jobname, "��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������", taskId, taskScheduling, null,"0Сʱ0��0��",buildResult.toString(),restartstatus);
		}
		//�ر�Appium������߳�
		if(as!=null){
			as.interrupt();
		}
	}

}
