SELECT resa.id_client,
       resa.id_room_reservation,
       room.id_room,
       resa.start_date,
       resa.end_date,
       SUM(room.night_price * DATEDIFF(resa.end_date, resa.start_date)) AS total_night_price
FROM room_reservation AS resa
         JOIN room_link_reservation AS link
              USING(id_room_reservation)
         JOIN room
              USING(id_room)
WHERE resa.id_room_reservation = 3
GROUP BY resa.id_client, resa.id_room_reservation, room.id_room, resa.start_date, resa.end_date;