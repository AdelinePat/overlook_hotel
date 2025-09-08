```Mermaid
---
config:
  layout: dagre
title: Overlook Hotel DB
---
erDiagram
	direction TB
	Client {
		int id_client  ""  
		string last_name  ""  
		string first_name  ""  
		string email  ""  
		string phone  ""  
	}
	Fidelity {
		int id_fidelity  ""  
		int id_client  ""  
		int pointValue  ""  
	}
	Room {
		int id_room  ""  
		int number  ""  
		int floor  ""  
		int capacity  ""  
		enum statut "available |  occupied  | cleaning"
        enum type "simple | double"
		float night_price  ""  
	}
	RoomReservation {
		int id_roomReservation  ""  
		int id_reservation  ""  
		int id_room  ""  
	}
	Reservation {
		int id_reservation  ""  
		int id_client  ""  
		datetime creation_date  ""  
		datetime start_date  ""  
		int nightNumber  ""  
		enum statut  "confirm | cancel" 
		datetime paymentDate  ""  
		enum payment  "pending | done"
		float totalPrice  ""  
	}
	Employee {
		int id_employee  ""  
		string last_name  ""  
		string first_name  ""  
		string email  ""  
		string job  ""  
	}
	Manager {
		int id_manager  ""  
		string last_name  ""  
		string first_name  ""  
		string email  ""  
	}
	Vacation {
		int id_vacation  ""  
		int id_employee  ""  
		datetime start  ""  
		datetime end  ""  
	}
	Formation {
		int id_formation  ""  
		datetime start  ""  
		datetime end  ""  
		string title  ""  
	}
	EmployeeFormation {
		int id_employeeFormation  ""  
		int id_formation  ""  
		int id_employee  ""  
	}
	Schedule {
		int id_schedule  ""  
		enum dayOfWeek  "mon, tues, wens, thur, fri, sat, sun"  
		decimal startShift  ""  
		decimal endShift  ""  
	}
	EmployeeSchedule {
		int id_employee  ""  
		int id_schedule  ""  
	}
    
    EventReservation {
        int id_event
        int id_client
        enum event  "wedding, party, birthday, burial, team meeting, other"
        datetime start  "with hour"
        datetime end "with hour"
        float totalPrice
    }

    EventReservationPlace {
        int id_eventPlace
        int id_event
        int id_place
    }

    Place {
        int id_place
        enum type "meeting room, swimming pool, spa, tennis ground, broomstick closet"
        float hourlyPrice
    }

    %% EventType {
    %%    int id_eventType
    %%    enum event  "wedding, party, birthday, burial, team meeting, other"
    %%    float majoration  "pourcentage entre 0 et 1 par rapport au type d'event"
    %%}

    FeedBack {
        int id_feedBack
        int id_client
        int rate " de 0 à 5"
        string comment
    }

    Response {
        int id_response
        int id_manager
        int id_feedback
        string answer
    }


	Client||--o|Fidelity:"possess"
	Client||--o{Reservation:"order"
	RoomReservation||--|{Room:"contain"
	RoomReservation||--|{Reservation:"concerns"
	Employee||--o{Vacation:"ask for"
	Employee||--o{EmployeeFormation:"follow"
	Formation||--|{EmployeeFormation:"concerns"
	Employee||--|{EmployeeSchedule:"get"
	Schedule||--|{EmployeeSchedule:"inform"

    Client ||--o{EventReservation:"order"
    EventReservationPlace ||--|{EventReservation:"contain"
    EventReservationPlace ||--|{Place:"define"

    Client ||--o{FeedBack : "write / rate"
    Manager ||--o{Response : "answer"
    FeedBack ||--||Response: "get"
```