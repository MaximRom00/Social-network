SET FOREIGN_KEY_CHECKS=0;

delete from user;

ALTER TABLE user AUTO_INCREMENT=1;

insert into user (email, is_active, name, password, role, is_enabled)
    VALUES
     ('admin@admin.com', true, 'Max','$2a$06$WlMimo64NIRof7k98/LwuufjF8Z4yV4ZZsUZ/7p4d4YoSXgzhrwye', 'ADMIN', true),
     ('user@user.by', true, 'user','$2a$06$MG61WvsbSj22vaZqGwdtS.nFMykYVzrr0cVui8GWofcSpzPh3OyHq', 'USER', true),
     ('dima@dima.by', true, 'Dima','$2a$06$CBAeDGcflJ/efolXphT7g.2Nxz.MERk9A8jCu3OnbOUK30mUsjkji', 'USER', true);


