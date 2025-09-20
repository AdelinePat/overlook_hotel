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

-- liste des employés avec leur job
SELECT e.id_employee, e.firstname, e.lastname, j.job_name
FROM employee AS e
         JOIN job AS j
              USING(id_job);

-- liste des eclients avec le nombre de réservations
SELECT c.id_client, c.firstname, c.lastname, COUNT(r.id_room_reservation) AS total_resa
FROM client AS c
         LEFT JOIN room_reservation AS r
                   USING(id_client)
GROUP BY c.id_client, c.firstname, c.lastname;