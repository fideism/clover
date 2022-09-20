package luckyclient.execution.webdriver;

import com.offbytwo.jenkins.model.BuildResult;
import luckyclient.execution.httpinterface.TestControl;
import luckyclient.execution.webdriver.ex.WebCaseExecution;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.api.serverOperation;
import luckyclient.remote.entity.*;
import luckyclient.tool.jenkins.BuildingInitialization;
import luckyclient.tool.mail.HtmlMail;
import luckyclient.tool.mail.MailSendInitialization;
import luckyclient.tool.shell.RestartServerInitialization;
import luckyclient.utils.LogUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class WebTestControl {

	/**
	 * ����̨ģʽ���ȼƻ�ִ������
	 * @param planname �ƻ�����
	 */
	public static void manualExecutionPlan(String planname) {
		// ������־�����ݿ�
		serverOperation.exetype = 1;
		String taskid = "888888";
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForLocal();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ʼ��WebDriver�����쳣��",e);
		}
		serverOperation caselog = new serverOperation();
		List<ProjectCase> testCases = GetServerApi.getCasesbyplanname(planname);
		List<ProjectCaseParams> pcplist = new ArrayList<>();
		if (testCases.size() != 0) {
			pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testCases.get(0).getProjectId()));
		}
		LogUtil.APP.info("��ǰ�ƻ��ж�ȡ����������{}����",testCases.size());
		int i = 0;
		for (ProjectCase testcase : testCases) {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			if (steps.size() == 0) {
				continue;
			}
			i++;
			LogUtil.APP.info("��ʼִ�е�{}������:��{}��......",i,testcase.getCaseSign());
			try {
				WebCaseExecution.caseExcution(testcase, steps, taskid, null,wd, caselog, pcplist);
			} catch (Exception e) {
				LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
			}
			LogUtil.APP.info("��ǰ����:��{}��ִ�����......������һ��",testcase.getCaseSign());
		}
		LogUtil.APP.info("��ǰ��Ŀ���Լƻ��е������Ѿ�ȫ��ִ�����...");
		// �ر������
		assert wd != null;
		wd.quit();
	}

	public static void taskExecutionPlan(TaskExecute task) {
		// ��¼��־�����ݿ�
		serverOperation.exetype = 0;
		String taskid = task.getTaskId().toString();
		TestControl.TASKID = taskid;
		String restartstatus = RestartServerInitialization.restartServerRun(taskid);
		BuildResult buildResult = BuildingInitialization.buildingRun(taskid);
		TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId());
		List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(task.getProjectId().toString());
		String projectname = taskScheduling.getProject().getProjectName();
		task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
		String jobname = taskScheduling.getSchedulingName();
		int drivertype = serverOperation.querydrivertype(taskid);
		int[] tastcount=new int[5];
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
					WebDriver wd = null;
					try {
						wd = WebDriverInitialization.setWebDriverForTask(drivertype);
					} catch (WebDriverException e1) {
						LogUtil.APP.error("��ʼ��WebDriver���� WebDriverException��", e1);
					} catch (IOException e2) {
						LogUtil.APP.error("��ʼ��WebDriver���� IOException��", e2);
					}

					serverOperation caselog = new serverOperation();
					List<ProjectCase> cases = GetServerApi.getCasesbyplanId(pp.getPlanId());
					LogUtil.APP.info("��ǰ���Լƻ� {} �й��С�{}��������������...",pp.getPlanName(),cases.size());
					LogUtil.APP.info("��ʼִ�е�ǰ���Լƻ� {} ......",pp.getPlanName());
					serverOperation.updateTaskExecuteStatusIng(taskid, caseCount);
					int i = 0;
					for (ProjectCase testcase : cases) {
						i++;
						LogUtil.APP.info("��ʼִ�е�ǰ�������� {} �ĵڡ�{}������������:��{}��......",task.getTaskName(),i,testcase.getCaseSign());
						List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
						if (steps.size() == 0) {
							continue;
						}
						try {
							// ���뿪ʼִ�е�����
							caselog.insertTaskCaseExecute(taskid, taskScheduling.getProjectId(),pp.getPlanId(),testcase.getCaseId(),testcase.getCaseSign(), testcase.getCaseName(), 4);
							WebCaseExecution.caseExcution(testcase, steps, taskid,pp.getPlanId(),wd, caselog, pcplist);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
						}
						LogUtil.APP.info("��ǰ����:��{}��ִ�����......������һ��",testcase.getCaseSign());
					}
					LogUtil.APP.info("��ǰ��{}�����Լƻ��е������Ѿ�ȫ��ִ�����...",pp.getPlanName());
					assert wd != null;
					wd.quit();
				}

				tastcount = serverOperation.updateTaskExecuteData(taskid, caseCount,2);
				tastcount[0]=caseCount;
				String testtime = serverOperation.getTestTime(taskid);
				LogUtil.APP.info("��ǰ��Ŀ��{}�����Լƻ��е������Ѿ�ȫ��ִ�����...",projectname);
				MailSendInitialization.sendMailInitialization(HtmlMail.htmlSubjectFormat(jobname),
						HtmlMail.htmlContentFormat(tastcount, taskid, buildResult.toString(), restartstatus, testtime, jobname),
						taskid, taskScheduling, tastcount,testtime,buildResult.toString(),restartstatus);
			} else {
				LogUtil.APP.warn("��Ŀ����ʧ�ܣ��Զ��������Զ��˳�����ǰ��JENKINS�м����Ŀ���������");
				MailSendInitialization.sendMailInitialization(jobname, "������Ŀ������ʧ�ܣ��Զ��������Զ��˳�����ǰȥJENKINS�鿴���������", taskid,
						taskScheduling, null,"0Сʱ0��0��",buildResult.toString(),restartstatus);
			}
		} else {
			LogUtil.APP.warn("��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������");
			MailSendInitialization.sendMailInitialization(jobname, "��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������", taskid,
					taskScheduling, null,"0Сʱ0��0��",buildResult.toString(),restartstatus);
		}
	}

}
