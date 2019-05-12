package org.dimensinfin.eveonline.neocom.sharedcode;

public enum RuntimeErrorInfo {
	  EXTRAS_NOT_DEFINED("[{0}]> 'onCreate' event did not received a valid set of 'extras'."
			, EExceptionSeverity.UNEXPECTED)
	, CREDENTIAL_NOT_FOUND("[{0}]> Credential id {1} not fount on the list of credentials."
			, EExceptionSeverity.UNEXPECTED);

	private String errorMessage;
	private EExceptionSeverity severity;

	RuntimeErrorInfo( final String errorMessage, final EExceptionSeverity severity ) {
		this.errorMessage = errorMessage;
		this.severity = severity;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public EExceptionSeverity getSeverity() {
		return severity;
	}
}
