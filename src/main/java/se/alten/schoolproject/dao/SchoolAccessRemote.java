package se.alten.schoolproject.dao;

import javax.ejb.Remote;
//denna klass utanför scopen för klassen. skit i denna
@Remote
public interface SchoolAccessRemote extends SchoolAccessLocal {
}
