package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.enums.SftpOp;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.converter.CryptoConverter;
import org.folio.rest.workflow.model.has.HasPassword;
import org.folio.rest.workflow.model.has.HasService;
import org.folio.rest.workflow.model.has.HasUsername;
import org.folio.rest.workflow.model.has.common.HasFtpTaskCommon;

@Entity
public class FtpTask extends AbstractTask implements DelegateTask, HasFtpTaskCommon, HasPassword, HasService, HasUsername {

  @Getter
  @Setter
  @Column(nullable = false)
  private String destinationPath;

  @Getter
  @Setter
  @Column(nullable = false)
  private String host;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SftpOp op;

  @Getter
  @Setter
  @Column(nullable = false)
  private String originPath;

  @Getter
  @Setter
  @Column(nullable = true)
  @Convert(converter = CryptoConverter.class)
  private String password;

  @Getter
  @Setter
  @Column(nullable = false)
  private Integer port;

  @Getter
  @Setter
  @Column(nullable = false)
  private String scheme;

  @Getter
  @Setter
  @Column(nullable = true)
  private String username;

  public FtpTask() {
    super();

    destinationPath = "";
    host = "";
    op = SftpOp.GET;
    originPath = "";
    port = 80;
    scheme = "";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (destinationPath == null) {
      destinationPath = "";
    }

    if (host == null) {
      host = "";
    }

    if (op == null) {
      op = SftpOp.GET;
    }

    if (originPath == null) {
      originPath = "";
    }

    if (port == null) {
      port = 80;
    }

    if (scheme == null) {
      scheme = "";
    }
  }

  @Override
  public String getBasePath() {
    // This is currently not used.
    return "";
  }

  @Override
  public void setBasePath(String basePath) {
    // This is currently not used.
  }

}
