package org.folio.rest.workflow.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.folio.rest.workflow.jms.EventProducer;
import org.folio.rest.workflow.model.repo.TriggerRepo;
import org.folio.spring.tenant.properties.TenantProperties;
import org.folio.spring.tenant.resolver.TenantHeaderResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(EventController.class)
class EventControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private EventController eventController;

  @MockBean
  private EventProducer eventProducer;

  @MockBean
  private TriggerRepo triggerRepo;

  @MockBean
  private TenantProperties tenantProperties;

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders.standaloneSetup(eventController)
        .setCustomArgumentResolvers(new TenantHeaderResolver("X-Okapi-Tenant"))
        .build();
  }

  @ParameterizedTest
  @MethodSource
  void upload(String tenant, String dir, String file, String expectedPath) throws Exception {
    mockMvc.perform(upload(tenant, dir, file))
        .andExpectAll(status().isOk(), jsonPath("inputFilePath").value(expectedPath));
    assertThat(readFile(expectedPath)).isEqualTo("This is the file content");
  }

  static Stream<Arguments> upload() {
    return Stream.of(
        arguments("diku", "d1" + File.separator + "d2" + File.separator + "d3", "filename.txt", "events" + File.separator + "diku" + File.separator + "d1" + File.separator + "d2" + File.separator + "d3" + File.separator + "filename.txt"),
        arguments("diku", "", "baz.txt", "events" + File.separator + "diku" + File.separator + "baz.txt"),
        arguments("foo", "a" + File.separator + ".." + File.separator + "b", "c" + File.separator + "d" + File.separator + ".." + File.separator + "e" + File.separator + "f.txt", "events" + File.separator + "foo" + File.separator + "b" + File.separator + "c" + File.separator + "e" + File.separator + "f.txt"));
  }

  @ParameterizedTest
  @MethodSource
  void uploadRejected(String tenant, String dir, String file) throws Exception {
    assertThrows(FileSystemException.class, () ->
        mockMvc.perform(upload(tenant, dir, file)));
  }

  static Stream<Arguments> uploadRejected() {
    return Stream.of(
        arguments("diku" + File.separator + ".." + File.separator + "x", "a", "a.txt"),
        arguments("diku" + File.separator + ".." + File.separator + ".." + File.separator + "x", "a", "a.txt"),
        arguments("diku", "a" + File.separator + ".." + File.separator + ".." + File.separator + "x", "a.txt"),
        arguments("diku", ".." + File.separator + "x", "a.txt"),
        arguments("diku", "a", ".." + File.separator + ".." + File.separator + "x.txt"),
        arguments("diku", "a", ".." + File.separator + ".." + File.separator + ".." + File.separator + "x.txt"),
        arguments("diku", "a", ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "x.txt"));
  }

  MockHttpServletRequestBuilder upload(String tenant, String dir, String file) throws Exception {
    var sampleFile = new MockMultipartFile(
        "file",
        file,
        MediaType.TEXT_PLAIN_VALUE,
        "This is the file content".getBytes());

    return MockMvcRequestBuilders.multipart("/events").file(sampleFile)
        .header("X-Okapi-Tenant", tenant).param("path", dir);
  }

  private static String readFile(String path) throws IOException {
    return FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
  }
}
