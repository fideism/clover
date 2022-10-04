package springboot.model;

import java.io.Serializable;

/**
 * ��������ʵ��
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 Seagull
 * =================================================================
 * @author Seagull
 * @date 2019��4��23��
 */
public class WebDebugCaseEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer caseId;
    private Integer userId;
    private String loadpath;
	//�޸ĵ�  HTTP�ӿ� 0,  Web UI, 1,   API����, 2,   �ƶ���, 3
	private Integer caseType;
	private Integer browserType;

	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLoadpath() {
		return loadpath;
	}
	public void setLoadpath(String loadpath) {
		this.loadpath = loadpath;
	}

	//�޸ĵ�
	public Integer getCaseType(){return caseType; }
	public void setCaseType(Integer caseType) {
		this.caseType = caseType;
	}
	public Integer getBrowserType() {
		return browserType;
	}
	public void setBrowserType(Integer browserType) {
		this.browserType = browserType;
	}
}
