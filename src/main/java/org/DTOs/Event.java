package org.DTOs;

public class Event {
  public int id;
  public String name;
  public String homeTeamName;
  public String endDate;
  public String venueID;
  public String venueName;
  public String venueSeoUrl;
  public String venueSeoTitle;
  public String seoUrl;

  public Event(int id, String name, String homeTeamName, String endDate, String venueID, String venueName,
    String venueSeoUrl, String venueSeoTitle, String seoUrl) {
    this.id = id;
    this.name = name;
    this.homeTeamName = homeTeamName;
    this.endDate = endDate;
    this.venueID = venueID;
    this.venueName = venueName;
    this.venueSeoUrl = venueSeoUrl;
    this.venueSeoTitle = venueSeoTitle;
    this.seoUrl = seoUrl;
  }
  public Event()
  {

  }
}