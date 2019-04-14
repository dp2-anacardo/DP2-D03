
package forms;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class PriorityForm {

	private int		id;
	private int		version;
	private String	titleES;
	private String	titleEN;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitleES() {
		return this.titleES;
	}

	public void setTitleES(final String titleES) {
		this.titleES = titleES;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitleEN() {
		return this.titleEN;
	}

	public void setTitleEN(final String titleEN) {
		this.titleEN = titleEN;
	}
}
