SELECT s.section_id,s.subject_id,s.schedule,r.room_name,r.capacity,s.faculty_number,f.firstname,f.lastname,s.version 
FROM sections s
LEFT OUTER JOIN rooms r ON (s.room_name = r.room_name) 
LEFT OUTER JOIN faculty f ON (s.faculty_number = f.faculty_number)
where s.section_id 
NOT IN 
(select e.section_id from enlistments e where e.student_number = ?)
