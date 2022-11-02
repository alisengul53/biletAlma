package org.example;

import org.DTOs.*;
import org.apiCalls.ApiCalls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    ApiCalls apiCalls = new ApiCalls();
    long tokenRefreshed = System.currentTimeMillis();
    apiCalls.login(System.getenv("username"), System.getenv("password"));
    tokenRefreshed = System.currentTimeMillis();
    System.out.println("Giriş yapıldı");
    Event bjkEvent = apiCalls.getEventByName(System.getenv("matchName"));
    EventDetailsDTO eventDetailsDTO = apiCalls.getEventDetailsById(bjkEvent);
    String str = System.getenv("desiredList");
    List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
    List<EventDetail> desiredList = new ArrayList<>();
    int listSize = eventDetailsDTO.value.categories.size();
    for(int t = 0; t < listSize; t++)
    {
      if(items.contains(String.valueOf(t+1)))
        desiredList.add(eventDetailsDTO.value.categories.get(t));
    }
    eventDetailsDTO.value.categories = desiredList;
    AvailableBlocksDTO availableBlocksDTO;
    AddToChartRequestDTO addToChartRequestDTO = new AddToChartRequestDTO();
    boolean giris = false;
    //apiCalls.login("Harunsengul53@icloud.com", "5020403Hm");
    boolean breakAllLoops = false;
    while(true)
    {
      if(giris)
      {
        apiCalls.login(System.getenv("username"), System.getenv("password"));
        tokenRefreshed = System.currentTimeMillis();
        System.out.println("Tekrar giriş yapıldı");
      }
      int count = 0;
      while(true)
      {
        for(int i = 0; i < eventDetailsDTO.value.categories.size(); i++)
        {
          availableBlocksDTO = apiCalls.getAvailableBlocks(eventDetailsDTO.value.id, eventDetailsDTO.value.categories.get(i).id);
          if(!availableBlocksDTO.valueList.isEmpty())
          {
            for(int k = 0; k < availableBlocksDTO.valueList.size(); k++)
            {
              try
              {
                List<AddToChartVariant> variantCount = new ArrayList<>();
                addToChartRequestDTO.blockId = availableBlocksDTO.valueList.get(k).id;
                addToChartRequestDTO.eventId = bjkEvent.id;
                addToChartRequestDTO.seatCategoryId = eventDetailsDTO.value.categories.get(i).id;
                addToChartRequestDTO.seatCategoryTicketTypeId = 100;
                addToChartRequestDTO.sidebyside = true;
                addToChartRequestDTO.discountIntegrationId = null;
                AddToChartVariant addToChartVariant = new AddToChartVariant();
                addToChartVariant.count = 1;
                VariantsList variantsList = apiCalls.getVariants(bjkEvent.id, eventDetailsDTO.value.categories.get(i).id);
                if(variantsList.value != null)
                {
                  addToChartVariant.seatCategoryVariantId = variantsList.value.variants.get(0).id;
                  variantCount.add(addToChartVariant);
                  addToChartRequestDTO.variantCount = variantCount;
                  apiCalls.addToChart(addToChartRequestDTO);
                  eventDetailsDTO.value.categories.remove(i);
                  System.out.println(String.format("Eklenenen bilet kategorisi: %s", availableBlocksDTO.valueList.get(k).name));
                  break;
                }
                else
                  breakAllLoops = true;
              }
              catch(Exception e)
              {
                System.out.println("Hata oldu: " + e.getMessage());
              }
              if(breakAllLoops)
                break;
            }
          }
          if(breakAllLoops)
            break;
        }
        if(breakAllLoops)
          break;
        count++;
        if(count == 100)
        {
          System.out.println("Her kategori 100'er defa daha denendi denendi bilet eklenemedi. Devam ediliyor");
          count = 0;
        }
        if(System.currentTimeMillis() - tokenRefreshed > TimeUnit.MINUTES.toMillis(18))
        {
          giris = true;
          break;
        }
      }
      if(breakAllLoops)
        break;
    }
  }

}

//class MultithreadingDemo extends Thread {
//  public EventDetail event;
//  public ApiCalls apiCalls;
//  public int categoryId;
//  public MultithreadingDemo(EventDetail event, ApiCalls apiCalls, int categoryId)
//  {
//    this.event = event;
//    this.apiCalls = apiCalls;
//    this.categoryId = categoryId;
//  }
//
//  public void run()
//  {
//    try {
//      var availableBlocksDTO = apiCalls.getAvailableBlocks(event.id, categoryId);
//    }
//    catch (Exception e) {
//      // Throwing an exception
//      System.out.println("Exception is caught");
//    }
//  }
//}

