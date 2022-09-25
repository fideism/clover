package luckyclient.remote.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * �������ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��13��
 */
public class TaskScheduling extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** ԤԼ����ID */
	private Integer schedulingId;
	/** ԤԼ�������� */
	private String schedulingName;
	/** ����ID */
	private Integer jobId;
	/** ��ĿID */
	private Integer projectId;
	/** ���Լƻ�ID */
	private Integer planId;
	/** �ۺϼƻ�ID */
	private Integer suiteId;
	/** �ͻ���ID */
	private Integer clientId;
	/** ���� */
	private String envName;
	/** �ʼ�֪ͨ��ַ */
	private String emailAddress;
	/** ���������͵�ַ */
	private String pushUrl;
	/** �����ʼ�֪ͨʱ�ľ����߼�, 0-ȫ����1-�ɹ���-1-ʧ�� */
	private Integer emailSendCondition;
	/** jenkins�������� */
	private String buildingLink;
	/** Զ��ִ��Shell�ű� */
	private String remoteShell;
	/** �ͻ���ִ���߳��� */
	private Integer exThreadCount;
	/** �������� 0 �ӿ� 1 Web UI 2 �ƶ� */
	private Integer taskType;
	/** UI�Զ������������ 0 IE 1 ��� 2 �ȸ� 3 Edge */
	private Integer browserType;
	/** �ƻ����� 1 ���ƻ� 2 �ۺϼƻ� */
	private Integer planType;
	/** ����ʱʱ��(����) */
	private Integer taskTimeout;
	/** �ͻ��˲�������׮·�� */
	private String clientDriverPath;
	/** ������Ŀʵ�� */
	private Project project;
	/** ������Ŀ�ƻ� */
	private ProjectPlan projectPlan;
	/** �����ۺϼƻ� */
	private ProjectSuite projectSuite;
	/** �������� */
	private String jobName;
	/** cronִ�б��ʽ */
	private String cronExpression;
	/** ����״̬��0���� 1��ͣ�� */
	private String status;

	public Integer getSchedulingId() {
		return schedulingId;
	}

	public void setSchedulingId(Integer schedulingId) {
		this.schedulingId = schedulingId;
	}

	public String getSchedulingName() {
		return schedulingName;
	}

	public void setSchedulingName(String schedulingName) {
		this.schedulingName = schedulingName;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public Integer getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(Integer suiteId) {
		this.suiteId = suiteId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public Integer getEmailSendCondition() {
		return emailSendCondition;
	}

	public void setEmailSendCondition(Integer emailSendCondition) {
		this.emailSendCondition = emailSendCondition;
	}

	public String getBuildingLink() {
		return buildingLink;
	}

	public void setBuildingLink(String buildingLink) {
		this.buildingLink = buildingLink;
	}

	public String getRemoteShell() {
		return remoteShell;
	}

	public void setRemoteShell(String remoteShell) {
		this.remoteShell = remoteShell;
	}

	public Integer getExThreadCount() {
		return exThreadCount;
	}

	public void setExThreadCount(Integer exThreadCount) {
		this.exThreadCount = exThreadCount;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getBrowserType() {
		return browserType;
	}

	public void setBrowserType(Integer browserType) {
		this.browserType = browserType;
	}

	public Integer getPlanType() {
		return planType;
	}

	public void setPlanType(Integer planType) {
		this.planType = planType;
	}

	public Integer getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(Integer taskTimeout) {
		this.taskTimeout = taskTimeout;
	}

	public String getClientDriverPath() {
		return clientDriverPath;
	}

	public void setClientDriverPath(String clientDriverPath) {
		this.clientDriverPath = clientDriverPath;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectPlan getProjectPlan() {
		return projectPlan;
	}

	public void setProjectPlan(ProjectPlan projectPlan) {
		this.projectPlan = projectPlan;
	}

	public ProjectSuite getProjectSuite() {
		return projectSuite;
	}

	public void setProjectSuite(ProjectSuite projectSuite) {
		this.projectSuite = projectSuite;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("schedulingId", getSchedulingId())
				.append("jobId", getJobId())
				.append("projectId", getProjectId())
				.append("planId", getPlanId())
				.append("suiteId", getSuiteId())
				.append("clientId", getClientId())
				.append("envName", getEnvName())
				.append("emailAddress", getEmailAddress())
				.append("pushUrl", getPushUrl())
				.append("emailSendCondition", getEmailSendCondition())
				.append("buildingLink", getBuildingLink())
				.append("remoteShell", getRemoteShell())
				.append("exThreadCount", getExThreadCount())
				.append("taskType", getTaskType())
				.append("planType", getPlanType())
				.append("browserType", getBrowserType())
				.append("taskTimeout", getTaskTimeout())
				.append("clientDriverPath", getClientDriverPath())
				.toString();
	}
}