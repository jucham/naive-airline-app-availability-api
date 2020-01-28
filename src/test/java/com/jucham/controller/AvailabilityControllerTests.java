package com.jucham.controller;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jucham.repository.FlightAvailabilityRepository;
import com.jucham.repository.entity.FlightAvailabilityEntity;
import com.jucham.service.FlightAvailabilityService;
import com.jucham.service.impl.FlightAvailabilityServiceImpl;
import com.jucham.util.LoadPayloadFromFile;
import com.jucham.util.LoadPayloadFromFileRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityControllerTests {

    @Rule
    public final LoadPayloadFromFileRule loadPayloadFromFileRule = new LoadPayloadFromFileRule();

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FlightAvailabilityRepository flightAvailabilityRepository;

    @Autowired
    FlightAvailabilityService flightAvailabilityService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    private MockMvc mockMvcWithDoc;

    @Before
    public void setUp() {
        this.mockMvcWithDoc = MockMvcBuilders
                .webAppContextSetup(context)
//              .addFilters(springSecurityFilterChain)
                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
                .alwaysDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        Preprocessors.preprocessRequest(),
                        Preprocessors.preprocessResponse(
                                ResponseModifyingPreprocessors.replaceBinaryContent(),
                                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                                Preprocessors.prettyPrint())))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080)
                        .and().snippets()
                        .withDefaults(CliDocumentation.curlRequest(),
                                HttpDocumentation.httpRequest(),
                                HttpDocumentation.httpResponse(),
                                AutoDocumentation.requestFields(),
                                AutoDocumentation.responseFields(),
                                AutoDocumentation.pathParameters(),
                                AutoDocumentation.requestParameters(),
                                AutoDocumentation.description(),
                                AutoDocumentation.methodAndPath(),
                                AutoDocumentation.section()))
                .build();
    }

    @Test
    @LoadPayloadFromFile
    @Transactional
    public void get_200_with_results_sorted_by_date() throws Exception {

        FlightAvailabilityEntity fae1 = getRandomValidFlightAvailabilityEntity();
        fae1.setFlightNumber(3000);
        fae1.setStartTime("11:00");
        FlightAvailabilityEntity fae2 = getRandomValidFlightAvailabilityEntity();
        fae2.setStartTime("10:00");
        fae2.setFlightNumber(2000);
        FlightAvailabilityEntity fae3 = getRandomValidFlightAvailabilityEntity();
        fae3.setStartTime("09:00");
        fae3.setFlightNumber(1000);
        List<FlightAvailabilityEntity> faes = new ArrayList<>();
        faes.add(fae1);
        faes.add(fae2);
        faes.add(fae3);
        flightAvailabilityRepository.saveAll(faes);

        this.mockMvcWithDoc.perform(RestDocumentationRequestBuilders.get("/flights/NCE/AMS/3019-09-15/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(loadPayloadFromFileRule.getPayload()));
    }

    @Test
    public void get_200_and_void_result_when_there_is_no_availability() throws Exception {
        this.mockMvc.perform(get("/flights/NCE/AMS/3019-09-15/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void get_200_when_json_media_type_is_requested() throws Exception {
        final MockHttpServletRequestBuilder request = get("/flights/NCE/AMS/3019-09-15/1").accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void get_406_error_when_xml_content_is_requested() throws Exception {
        final MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders.get("/flights/NCE/AMS/3019-09-15/1").accept(MediaType.APPLICATION_XML);
        this.mockMvcWithDoc.perform(request)
                .andExpect(status().is(406))
                .andExpect(content().json("{\"error\": \"not_acceptable\", \"errorDescription\": \"acceptable MIME types: [application/json, application/*+json]\"}"));
    }

    @Test
    public void get_400_error_when_origin_has_invalid_length() throws Exception {
        final MockHttpServletRequestBuilder request = get("/flights/XX/AMS/3019-09-15/1");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"XX is an invalid value for originAirportCode\"}"));
    }

    @Test
    public void get_400_error_when_origin_has_invalid_chars() throws Exception {
        final MockHttpServletRequestBuilder request = get("/flights/NC3/AMS/3019-09-15/1");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"NC3 is an invalid value for originAirportCode\"}"));
    }

    @Test
    public void get_400_error_when_destination_has_invalid_length() throws Exception {
        final MockHttpServletRequestBuilder request = get("/flights/NCE/YY/3019-09-15/1");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"YY is an invalid value for destinationAirportCode\"}"));
    }

    @Test
    public void get_400_error_when_destination_has_invalid_chars() throws Exception {
        final MockHttpServletRequestBuilder request = get("/flights/NCE/4MS/3019-09-15/1");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"4MS is an invalid value for destinationAirportCode\"}"));
    }

    @Test
    public void get_400_error_when_start_date_is_not_valid() throws Exception {
        this.mockMvc.perform(get("/flights/NCE/AMS/3019-09-155/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"3019-09-155 is an invalid value for startDate\"}"));
    }

    @Test
    public void get_400_error_when_start_date_is_in_past() throws Exception {
        final MockHttpServletRequestBuilder request = get("/flights/NCE/AMS/" + LocalDate.now().minusDays(1) + "/1");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{error:\"invalid_request\",errorDescription:\"startDate must not be in the past\"}"));
    }

    @Test
    public void get_400_error_when_number_of_passengers_is_invalid() throws Exception {
        MockHttpServletRequestBuilder request = get("/flights/NCE/AMS/3019-09-15/aa");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"aa is an invalid value for numberOfPassengers\"}"));
    }

    @Test
    public void get_400_error_when_number_of_passengers_is_too_small() throws Exception {
        MockHttpServletRequestBuilder request = get("/flights/NCE/AMS/3019-09-15/0");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"0 is an invalid value for numberOfPassengers\"}"));
    }

    @Test
    public void get_400_error_when_number_of_passengers_is_too_big() throws Exception {
        MockHttpServletRequestBuilder request = get("/flights/NCE/AMS/3019-09-15/13");
        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"invalid_request\", \"errorDescription\": \"13 is an invalid value for numberOfPassengers\"}"));
    }

    @Test
    public void get_500_error_when_there_is_problem_on_server_side() throws Exception {
        MockHttpServletRequestBuilder request = get("/flights/NCE/AMS/3019-09-15/1");

        FlightAvailabilityServiceImpl flightAvailabilityServiceImpl = (FlightAvailabilityServiceImpl) this.flightAvailabilityService;
        FlightAvailabilityRepository concreteRepo = (FlightAvailabilityRepository) ReflectionTestUtils.getField(flightAvailabilityServiceImpl, "flightAvailabilityRepository");
        FlightAvailabilityRepository mockedRepo = Mockito.mock(FlightAvailabilityRepository.class);

        when(mockedRepo.findByOriginAirportCodeIgnoreCaseAndDestinationAirportCodeIgnoreCase(anyString(), anyString()))
                .thenThrow(NullPointerException.class);

        ReflectionTestUtils.setField(flightAvailabilityServiceImpl, "flightAvailabilityRepository", mockedRepo);

        this.mockMvc.perform(request).andExpect(status()
                .isInternalServerError())
                .andExpect(content().json("{\"error\":\"internal_error\",\"errorDescription\":\"Internal error from server\"}"));

        //restore the real repo
        ReflectionTestUtils.setField(flightAvailabilityServiceImpl, "flightAvailabilityRepository", concreteRepo);

    }

    public FlightAvailabilityEntity getRandomValidFlightAvailabilityEntity() {
        FlightAvailabilityEntity fae = new FlightAvailabilityEntity();
        fae.setFlightNumber(1234);
        fae.setStartTime("16:30");
        fae.setAircraftType("A350");
        fae.setPricePerPassenger(new BigDecimal("120"));
        fae.setRemainingSeats(42);
        fae.setOriginAirportCode("NCE");
        fae.setDestinationAirportCode("AMS");
        fae.setDuration(90);
        return fae;
    }

}
