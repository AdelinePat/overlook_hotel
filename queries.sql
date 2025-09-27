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

SELECT
    r.id_room,
    r.night_price,
    (r.night_price + COALESCE(SUM(rb.daily_price), 0)) AS total_price
FROM room AS r
         LEFT JOIN room_link_bonus AS rlb USING(id_room)
         LEFT JOIN room_bonus AS rb USING(id_room_bonus)
GROUP BY r.id_room, r.night_price
HAVING (r.night_price + COALESCE(SUM(rb.daily_price), 0)) >= 150;

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
    WHERE resa.id_client = 1
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

-- RECUPERER CHAMBRE AVEC BONUS DE BASE
SELECT * FROM room AS r
LEFT JOIN room_link_bonus AS rlb USING(id_room)
LEFT JOIN room_bonus AS rb USING(id_room_bonus)
WHERE rb.type = 'SECHOIR';

-- FILTRE PAR PRIX
SELECT r.*
FROM room r
WHERE r.night_price + COALESCE(
    (
        SELECT SUM(rb.daily_price)
        FROM room_bonus rb
        INNER JOIN room_link_bonus rlb
            ON rb.id_room_bonus = rlb.id_room_bonus
        WHERE rlb.id_room = r.id_room
    ),
    0
) >= 150;


SELECT
    r.id_room,
    r.night_price,
    (r.night_price + COALESCE(SUM(rb.daily_price), 0)) AS total_price
FROM room AS r
LEFT JOIN room_link_bonus AS rlb USING(id_room)
LEFT JOIN room_bonus AS rb USING(id_room_bonus)
GROUP BY r.id_room, r.night_price
HAVING (r.night_price + COALESCE(SUM(rb.daily_price), 0)) >= 150;

-- requete panier evenements
SELECT id_event_reservation, event, total_price
FROM event_reservation
ORDER BY id_event_reservation DESC
LIMIT 5;

-- panier avec date et lieu

SELECT
    id_event_reservation,
    event,
    start_date,
    end_date,
    total_price,
    name AS place_type
FROM event_reservation
JOIN event_link_place USING (id_event_reservation)
JOIN place USING (id_place)
JOIN place_type USING (id_place_type)
ORDER BY id_event_reservation DESC
LIMIT 5;



-- Get confirmed reservations for client 1
SELECT
    rr.id_room_reservation,
    rr.id_client,
    c.lastname,
    r.id_room,
    r.number,
    r.night_price,
    (r.night_price + COALESCE(SUM(DISTINCT rb.daily_price), 0) + COALESCE(SUM(DISTINCT rrb_bonus.daily_price), 0)) AS total_price
FROM room_reservation rr
JOIN client c ON rr.id_client = c.id_client
JOIN room_link_reservation rlr ON rlr.id_room_reservation = rr.id_room_reservation
JOIN room r ON r.id_room = rlr.id_room
LEFT JOIN room_link_bonus rlb ON rlb.id_room = r.id_room
LEFT JOIN room_bonus rb ON rb.id_room_bonus = rlb.id_room_bonus
LEFT JOIN room_reservation_bonus rrb ON rrb.id_room_reservation = rr.id_room_reservation AND rrb.id_room = r.id_room
LEFT JOIN room_bonus rrb_bonus ON rrb_bonus.id_room_bonus = rrb.id_room_bonus
WHERE c.email = 'quentin.colin@example.com'
GROUP BY rr.id_room_reservation, rr.id_client, c.lastname, r.id_room, r.number, r.night_price;