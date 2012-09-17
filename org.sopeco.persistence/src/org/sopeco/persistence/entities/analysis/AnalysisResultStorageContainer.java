package org.sopeco.persistence.entities.analysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Stores any analysis result that implements {@link IStorableAnalysisResult}.
 * 
 * @author Dennis Westermann
 * 
 */
@Entity
public class AnalysisResultStorageContainer implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "resultId")
	private String resultId;


	@Lob
	@Column(name = "resultObject")
	private IStorableAnalysisResult resultObject;

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public IStorableAnalysisResult getResultObject() {
		return resultObject;
	}

	public void setResultObject(IStorableAnalysisResult resultObject) {
		this.resultObject = resultObject;
	}

}
