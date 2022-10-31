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
    apiCalls.login("Harunsengul53@icloud.com", "5020403Hm");
    boolean breakAllLoops = false;
    Event bjkEvent = apiCalls.getEventByName("Trabzonspor A.Ş.");
    EventDetailsDTO eventDetailsDTO = apiCalls.getEventDetailsById(bjkEvent);
    AvailableBlocksDTO availableBlocksDTO;
    AddToChartRequestDTO addToChartRequestDTO = new AddToChartRequestDTO();
    while(true)
    {
      if(System.currentTimeMillis() - tokenRefreshed > TimeUnit.MINUTES.toMillis(18))
      {
        apiCalls.login("Harunsengul53@icloud.com", "5020403Hm");
        tokenRefreshed = System.currentTimeMillis();
      }
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

