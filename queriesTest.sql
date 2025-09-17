SELECT  id_room_reservation, firstname, lastname, payment_date
FROM room_reservation
JOIN client USING(id_client)
ORDER BY id_room_reservation;