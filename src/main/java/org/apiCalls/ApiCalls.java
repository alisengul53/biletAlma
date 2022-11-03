package org.apiCalls;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.DTOs.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ApiCalls {
  HttpClient httpClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(Duration.ofSeconds(10))
    .build();
  ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
  ObjectMapper mapper = new ObjectMapper();
  public static String accessToken = "";
  public LoginResponseDTO login(String username, String password)
  {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password, true);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      for(int i = 0; i < 3; i++)
      {
        String json = ow.writeValueAsString(loginRequestDTO);
        HttpRequest request = HttpRequest.newBuilder()
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .uri(URI.create("https://ticketingweb.passo.com.tr/api/passoweb/login"))
          .header("Content-Type", "application/json")
          .build();
        HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200)
        {
          LoginResponseDTO responseDto = mapper.readValue(response.body(), LoginResponseDTO.class);
          accessToken = responseDto.value.access_token;
          return responseDto;
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public EventListDTO getAllFootballEvent()
  {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString("{\"GenreId\":\"4615\",\"LanguageId\":118,\"from\":0,\"size\":53}"))
        .uri(URI.create("https://ticketingweb.passo.com.tr/api/passoweb/allevents"))
        .header("Content-Type", "application/json")
        .build();
      HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());
      EventListDTO responseDto = mapper.readValue(response.body(), EventListDTO.class);
      return responseDto;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Event getEventByName(String name)
  {
    EventListDTO eventList = getAllFootballEvent();
    for(Event event: eventList.valueList)
    {
      if(event.name.contains(name))
        return event;
    }
    return null;
  }

  public EventDetailsDTO getEventDetailsById(Event event)
  {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(String.format("https://ticketingweb.passo.com.tr/api/passoweb/geteventdetails/%s/%s/118",event.seoUrl,event.id)))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + accessToken)
        .build();
      HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());
      EventDetailsDTO responseDto = mapper.readValue(response.body(), EventDetailsDTO.class);
      return responseDto;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public VariantsList getVariants(int eventId, int seatCategoryId)
  {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(String.format("https://ticketingweb.passo.com.tr/api/passoweb/getvariants?eventId=%s&&serieId=&tickettype=100&campaignId=null&validationintegrationid=null&seatcategoryid=%s", eventId, seatCategoryId)))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + accessToken)
        .build();
      HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());
      VariantsList responseDto = mapper.readValue(response.body(), VariantsList.class);
      return responseDto;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public AvailableBlocksDTO getAvailableBlocks(int eventId, int seatCategoryId)
  {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(String.format("https://ticketingweb.passo.com.tr/api/passoweb/getavailableblocklist?eventId=%s&seatCategoryId=%s", eventId, seatCategoryId)))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + accessToken)
        .build();
      HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());
      AvailableBlocksDTO responseDto = mapper.readValue(response.body(), AvailableBlocksDTO.class);
      return responseDto;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public AddToChartResponseDTO addToChart(AddToChartRequestDTO addToChartRequestDTO)
  {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(ow.writeValueAsString(addToChartRequestDTO)))
        .uri(URI.create("https://ticketingweb.passo.com.tr/api/passoweb/addbestseatstobasket"))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + accessToken)
        .build();
      HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());
      AddToChartResponseDTO responseDto = mapper.readValue(response.body(), AddToChartResponseDTO.class);
      if(response.statusCode() == 200)
      {
        System.out.println("Bilet sepete eklendi");
        System.out.println("Response: " + response);
      }
      return responseDto;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }
}
