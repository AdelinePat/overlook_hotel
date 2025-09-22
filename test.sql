-- requette pour recuperer lieu, capacité et prix des chambres
SELECT p.id_place, pt.name AS type_lieu, p.capacity, p.hourly_price
FROM place p
JOIN place_type pt ON pt.id_place_type = p.id_place_type
WHERE LOWER(pt.name) LIKE '%salle%'
  AND p.capacity >= 10
  AND p.hourly_price BETWEEN 20 AND 50
  AND NOT EXISTS (
    SELECT 1
    FROM event_link_place elp
    JOIN event_reservation er ON er.id_event_reservation = elp.id_event_reservation
    WHERE elp.id_place = p.id_place
      AND er.start_date < '2025-09-30 18:00:00'
      AND er.end_date   > '2025-09-30 14:00:00'
  );
