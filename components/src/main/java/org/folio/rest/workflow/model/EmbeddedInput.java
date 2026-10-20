package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import org.folio.rest.workflow.enums.InputAttribute;
import org.folio.rest.workflow.enums.InputType;
import org.folio.rest.workflow.model.converter.InputAttributeListConverter;
import org.folio.rest.workflow.model.converter.StringListConverter;
import org.folio.rest.workflow.model.has.common.HasEmbeddedInputCommon;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class EmbeddedInput implements HasEmbeddedInputCommon {

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(columnDefinition = "TEXT", nullable = true)
  @Convert(converter = InputAttributeListConverter.class)
  private List<InputAttribute> attributes;

  @Column(nullable = true)
  private String defaultValue;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  private String fieldId;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  private String fieldLabel;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("'TEXT'")
  @Enumerated(EnumType.STRING)
  private InputType inputType;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(columnDefinition = "TEXT", nullable = true)
  @Convert(converter = StringListConverter.class)
  private List<String> options;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean required;

  public EmbeddedInput() {
    super();

    attributes = new ArrayList<>();
    fieldId = "";
    fieldLabel = "";
    inputType = InputType.TEXT;
    options = new ArrayList<>();
    required = false;
  }

  /**
   * Perform pre-persist setup to ensure good state.
   *
   * Embeddables do not utilize @PrePersist annotation due to it not working well in certain circumstances.
   * Therefore, this must be manually called by the class utilizing this class.
   */
  public void prePersist() {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }

    if (fieldId == null) {
      fieldId = "";
    }

    if (fieldLabel == null) {
      fieldLabel = "";
    }

    if (inputType == null) {
      inputType = InputType.TEXT;
    }

    if (options == null) {
      options = new ArrayList<>();
    }

    if (required == null) {
      required = false;
    }
  }

  @Override
  public List<InputAttribute> getAttributes() {
    return attributes;
  }

  @Override
  public String getDefaultValue() {
    return defaultValue;
  }

  @Override
  public String getFieldId() {
    return fieldId;
  }

  @Override
  public String getFieldLabel() {
    return fieldLabel;
  }

  @Override
  public InputType getInputType() {
    return inputType;
  }

  @Override
  public List<String> getOptions() {
    return options;
  }

  @Override
  public Boolean getRequired() {
    return required;
  }

  @Override
  public void setAttributes(List<InputAttribute> attributes) {
    this.attributes = attributes;
  }

  @Override
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  @Override
  public void setFieldLabel(String fieldLabel) {
    this.fieldLabel = fieldLabel;
  }

  @Override
  public void setInputType(InputType inputType) {
    this.inputType = inputType;
  }

  @Override
  public void setOptions(List<String> options) {
    this.options = options;
  }

  @Override
  public void setRequired(Boolean required) {
    this.required = required;
  }

}
