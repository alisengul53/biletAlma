package org.DTOs;

import java.util.List;

public class AddToChartResponseDTO {
  public int totalItemCount;
  public List<AddChartResponseValueList> valueList;
  public boolean isError;
  public int resultCode;
}
