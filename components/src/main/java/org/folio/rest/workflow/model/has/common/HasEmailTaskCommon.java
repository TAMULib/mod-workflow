package org.folio.rest.workflow.model.has.common;

/**
 * This interface provides common methods for {@link org.folio.rest.workflow.model.EmailTask EmailTask}.
 */
public interface HasEmailTaskCommon {

  public String getAttachmentPath();
  public String getIncludeAttachment();
  public String getMailBcc();
  public String getMailCc();
  public String getMailFrom();
  public String getMailMarkup();
  public String getMailSubject();
  public String getMailText();
  public String getMailTo();

  public void setAttachmentPath(String attachmentPath);
  public void setIncludeAttachment(String includeAttachment);
  public void setMailBcc(String mailBcc);
  public void setMailCc(String mailCc);
  public void setMailFrom(String mailFrom);
  public void setMailMarkup(String mailMarkup);
  public void setMailSubject(String mailSubject);
  public void setMailText(String mailText);
  public void setMailTo(String mailTo);
}
