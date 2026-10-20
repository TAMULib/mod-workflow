package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasEmailTaskCommon;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class EmailTask extends AbstractTask implements DelegateTask, HasEmailTaskCommon {

  @Column(columnDefinition = "TEXT", nullable = true)
  private String attachmentPath;

  // Must be String rather than Boolean to allow freemarker translation of variables.
  @Column(nullable = false)
  @ColumnDefault("'false'")
  private String includeAttachment;

  @Column(nullable = true)
  private String mailBcc;

  @Column(nullable = true)
  private String mailCc;

  @Column(nullable = false)
  private String mailFrom;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String mailText;

  @Column(nullable = false)
  private String mailTo;

  @Column(columnDefinition = "TEXT", nullable = true)
  private String mailMarkup;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String mailSubject;

  public EmailTask() {
    super();

    includeAttachment = "false";
    mailFrom = "";
    mailText = "";
    mailTo = "";
    mailSubject = "";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (includeAttachment == null) {
      includeAttachment = "false";
    }

    if (mailFrom == null) {
      mailFrom = "";
    }

    if (mailText == null) {
      mailText = "";
    }

    if (mailTo == null) {
      mailTo = "";
    }

    if (mailSubject == null) {
      mailSubject = "";
    }
  }

  @Override
  public String getAttachmentPath() {
    return attachmentPath;
  }

  @Override
  public String getIncludeAttachment() {
    return includeAttachment;
  }

  @Override
  public String getMailBcc() {
    return mailBcc;
  }

  @Override
  public String getMailCc() {
    return mailCc;
  }

  @Override
  public String getMailFrom() {
    return mailFrom;
  }

  @Override
  public String getMailMarkup() {
    return mailMarkup;
  }

  @Override
  public String getMailSubject() {
    return mailSubject;
  }

  @Override
  public String getMailText() {
    return mailText;
  }

  @Override
  public String getMailTo() {
    return mailTo;
  }

  @Override
  public void setAttachmentPath(String attachmentPath) {
    this.attachmentPath = attachmentPath;
  }

  @Override
  public void setIncludeAttachment(String includeAttachment) {
    this.includeAttachment = includeAttachment;
  }

  @Override
  public void setMailBcc(String mailBcc) {
    this.mailBcc = mailBcc;
  }

  @Override
  public void setMailCc(String mailCc) {
    this.mailCc = mailCc;
  }

  @Override
  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  @Override
  public void setMailMarkup(String mailMarkup) {
    this.mailMarkup = mailMarkup;
  }

  @Override
  public void setMailSubject(String mailSubject) {
    this.mailSubject = mailSubject;
  }

  @Override
  public void setMailText(String mailText) {
    this.mailText = mailText;
  }

  @Override
  public void setMailTo(String mailTo) {
    this.mailTo = mailTo;
  }

}
