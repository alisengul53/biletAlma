package org.DTOs;

import java.util.List;

public class AddToChartRequestDTO {
  public int blockId;
  public int seatCategoryTicketTypeId;
  public int seatCategoryId;
  public boolean sidebyside;
  public String discountIntegrationId;
  public List<AddToChartVariant> variantCount;
  public int eventId;
}
