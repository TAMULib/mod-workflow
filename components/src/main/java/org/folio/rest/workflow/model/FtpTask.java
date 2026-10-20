package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.SftpOp;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.HasPassword;
import org.folio.rest.workflow.model.has.HasService;
import org.folio.rest.workflow.model.has.HasUsername;
import org.folio.rest.workflow.model.has.common.HasFtpTaskCommon;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class FtpTask extends AbstractTask implements DelegateTask, HasFtpTaskCommon, HasPassword, HasService, HasUsername {

  @Column(nullable = false)
  private String destinationPath;

  @Column(nullable = false)
  private String host;

  @Column(nullable = false)
  @ColumnDefault("'GET'")
  @Enumerated(EnumType.STRING)
  private SftpOp op;

  @Column(nullable = false)
  private String originPath;

  @Column(nullable = true)
  private String password;

  @Column(nullable = false)
  @ColumnDefault("80")
  private Integer port;

  @Column(nullable = false)
  @ColumnDefault("'ftp'")
  private String scheme;

  @Column(nullable = true)
  private String username;

  public FtpTask() {
    super();

    destinationPath = "";
    host = "";
    op = SftpOp.GET;
    originPath = "";
    port = 80;
    scheme = "ftp";
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
      scheme = "ftp";
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

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getHost() {
    return host;
  }

  @Override
  public Integer getPort() {
    return port;
  }

  @Override
  public String getScheme() {
    return scheme;
  }

  @Override
  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getDestinationPath() {
    return destinationPath;
  }

  @Override
  public SftpOp getOp() {
    return op;
  }

  @Override
  public String getOriginPath() {
    return originPath;
  }

  @Override
  public void setDestinationPath(String destinationPath) {
    this.destinationPath = destinationPath;
  }

  @Override
  public void setOp(SftpOp op) {
    this.op = op;
  }

  @Override
  public void setOriginPath(String originPath) {
    this.originPath = originPath;
  }

}
