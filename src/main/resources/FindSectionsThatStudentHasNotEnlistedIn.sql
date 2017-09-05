SELECT * FROM sections s natural join rooms natural join faculty where s.section_id NOT IN (select e.section_id from enlistments e where e.student_number = ?)
