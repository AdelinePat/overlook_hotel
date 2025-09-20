--  SELECT rr.id_room_reservation, rr.start_date, rr.end_date, rlr.id_room
--  FROM room_reservation as rr
--           JOIN room_link_reservation as rlr
--                USING(id_room_reservation)
--  WHERE NOT (rr.start_date <= '2025-09-08'
--      AND rr.end_date >= '2025-09-06');

--SELECT cl.lastname, r.id_room AS numero_chambre, s.name AS standing, rr.start_date AS debut, rr.end_date AS fin
--FROM room AS r
--LEFT JOIN room_link_reservation AS rlr
--    USING(id_room)
--LEFT JOIN room_reservation AS rr
--    USING(id_room_reservation)
--LEFT JOIN standing AS s
--    USING(id_standing)
--LEFT JOIN client as cl
--    USING (id_client)
--WHERE NOT (rr.start_date <= '2025-09-08'
--    AND rr.end_date >= '2025-09-06');

SELECT r.id_room, s.name, rr.start_date, rr.end_date FROM room AS r
LEFT JOIN standing AS s USING(id_standing)
LEFT JOIN room_link_reservation AS rlr USING(id_room)
LEFT JOIN room_reservation AS rr USING(id_room_reservation)
WHERE NOT EXISTS (
    SELECT 1
    FROM room_link_reservation rlr
    LEFT JOIN room_reservation rr USING (id_room_reservation)
    WHERE rlr.id_room = r.id_room
    AND rr.start_date <= '2025-09-08'
    AND rr.end_date >= '2025-09-06'
);

SELECT * FROM room AS r
WHERE NOT EXISTS (
    SELECT 1
    FROM room_link_reservation rlr
    LEFT JOIN room_reservation rr USING (id_room_reservation)
    WHERE rlr.id_room = r.id_room
    AND rr.start_date <= '2025-09-08'
    AND rr.end_date >= '2025-09-06'
);

SELECT id_room_reservation, start_date, end_date, rlr.id_room FROM room_reservation
JOIN room_link_reservation rlr USING(id_room_reservation);