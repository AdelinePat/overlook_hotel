-- DESCRIPTIF D'UNE RESERVATION (ex PANIER client)
-- SELECT resa.id_room_reservation,
--        resa.start_date,
--        resa.end_date,
--        (SUM(night_price) * DATEDIFF(resa.end_date, resa.start_date))
--            AS total_price
-- FROM room_reservation AS resa
--          JOIN room_link_reservation AS link
--               USING(id_room_reservation)
--          JOIN room
--               USING(id_room)
-- WHERE id_client = 1
-- GROUP BY resa.id_room_reservation, resa.start_date, resa.end_date;

SELECT * FROM client WHERE phone LIKE '%123456789%';