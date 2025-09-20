-- liste des chambres réservées du 6 au 8 septembre
SELECT rr.id_room_reservation, rr.start_date, rr.end_date, rlr.id_room
FROM room_reservation AS rr
         JOIN room_link_reservation AS rlr
              ON rr.id_room_reservation = rlr.id_room_reservation
WHERE rr.start_date <= '2025-09-08'
  AND rr.end_date >= '2025-09-06';


-- liste des chambres dispo du 6 au 8 septembre
SELECT rr.id_room_reservation, rr.start_date, rr.end_date, rlr.id_room
FROM room_reservation as rr
         JOIN room_link_reservation as rlr
              USING(id_room_reservation)
WHERE NOT (rr.start_date <= '2025-09-08'
    AND rr.end_date >= '2025-09-06');

--- !:!!!!!!!!!!!!!!!!! VERITABLE QUERY POUR AVOIR LES CHAMBRES DISPONIBLES !!!
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

-- CHAMBRE DISPO AVEC NOM CLIENT EN PLUS
SELECT cl.lastname, r.id_room AS numero_chambre, s.name AS standing, rr.start_date AS debut, rr.end_date AS fin
FROM room AS r
LEFT JOIN room_link_reservation AS rlr
    USING(id_room)
LEFT JOIN room_reservation AS rr
    USING(id_room_reservation)
LEFT JOIN standing AS s
    USING(id_standing)
LEFT JOIN client as cl
    USING (id_client)
WHERE NOT (rr.start_date <= '2025-09-08'
    AND rr.end_date >= '2025-09-06');


-- LISTE TOTAL RESERVATION PAR CLIENT + PRIX TOTAL
-- récupère total d'une réservation d'un client (attention peut être doit changer le WHERE)
SELECT resa.id_room_reservation,
       resa.start_date,
       resa.end_date,
       (SUM(night_price) * DATEDIFF(resa.end_date, resa.start_date))
           AS total_price
FROM room_reservation AS resa
         JOIN room_link_reservation AS link
              USING(id_room_reservation)
         JOIN room
              USING(id_room)
WHERE id_client = 2
GROUP BY resa.id_room_reservation, resa.start_date, resa.end_date;

-- UNE RESERVATION (par client) resa + prix total
    SELECT resa.id_client,
           resa.id_room_reservation,
           resa.start_date,
           resa.end_date,
           (SUM(night_price) * DATEDIFF(resa.end_date, resa.start_date))
               AS total_price
    FROM room_reservation AS resa
             JOIN room_link_reservation AS link
                  USING(id_room_reservation)
             JOIN room
                  USING(id_room)
    WHERE resa.id_room_reservation = 2
    GROUP BY resa.id_client, resa.id_room_reservation, resa.start_date, resa.end_date;


-- DESCRIPTIF D'UNE RESERVATION (ex PANIER client)
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
WHERE resa.id_room_reservation = 2
GROUP BY resa.id_client, resa.id_room_reservation, room.id_room, resa.start_date, resa.end_date;


-- une resa avec le nom du client en plus
  SELECT resa.id_client,
           c.lastname,
           c.firstname,
           resa.id_room_reservation,
           resa.start_date,
           resa.end_date,
           (SUM(night_price) * DATEDIFF(resa.end_date, resa.start_date))
               AS total_price
    FROM room_reservation AS resa
             JOIN room_link_reservation AS link
                  USING(id_room_reservation)
             JOIN room
                  USING(id_room)
             JOIN client AS c
                  USING(id_client)
    WHERE resa.id_room_reservation = 2
    GROUP BY resa.id_client, resa.id_room_reservation, resa.start_date, resa.end_date, c.lastname, c.firstname;