create table Authors (Author_id integer not null, Author varchar(255), primary key (Author_id));
create table Climate (Climate_id integer not null, Climate_abbreviation varchar(255), Climate_description varchar(255), primary key (Climate_id));
create table Place_of_Publication (Place_of_publication_id integer not null, Place_of_publication varchar(255), primary key (Place_of_publication_id));
create table Plant_Author (Plant_name_id integer not null, Author_id integer not null, Author_type_id varchar(255) not null, primary key (Plant_name_id, Author_type_id));
create table Plant_Citation (Plant_name_id integer not null, Publication_edition_id integer not null, primary key (Plant_name_id, Publication_edition_id));
create table Plant_Locality (Plant_locality_id integer not null, Area_code_L3 varchar(2), Region_code_L2 integer, Plant_name_id integer, primary key (Plant_locality_id));
create table Publication (Publication_id integer not null, Author varchar(255), Published varchar(255), Full_title varchar(255), Publication_type_id integer, primary key (Publication_id));
create table Publication_Edition (Publication_edition_id integer not null, Article_author varchar(255), Article_title varchar(255), Publication_id integer, primary key (Publication_edition_id));
create table vwMonocot_Name (Plant_name_id integer not null, Date_deleted datetime, Date_of_entry datetime, Date_modified datetime, Family varchar(255), Genus varchar(255), Genus_hybrid_marker varchar(255), Infraspecific_epithet varchar(255), Infraspecific_rank varchar(255), Full_epithet varchar(255), Institute_id varchar(255), Publication_author varchar(255), First_published varchar(255), Species varchar(255), Species_hybrid_marker varchar(255), Taxon_status_id varchar(255), Volume_and_page varchar(255), Accepted_plant_name_id integer, Basionym_id integer, Climate_id integer, Place_of_publication_id integer, primary key (Plant_name_id));
create index FKA645061F79361E8B on Plant_Author (Author_id);
create index FKA645061FBBAC6574 on Plant_Author (Plant_name_id);
create index FK7DB31A7B4638E194 on Plant_Citation (Publication_edition_id);
create index FK7DB31A7BBBAC6574 on Plant_Citation (Plant_name_id);
create index FK44FD0CF7BBAC6574 on Plant_Locality (Plant_name_id);
create index FK68B0B26B51A05C29 on Publication_Edition (Publication_id);
create index FK32FCC46339DBD09 on vwMonocot_Name (Climate_id);
create index FK32FCC46285A4A5B on vwMonocot_Name (Place_of_publication_id);
create index FK32FCC46C1A8E6DC on vwMonocot_Name (Accepted_plant_name_id);
create index FK32FCC4616DB072B on vwMonocot_Name (Basionym_id);
alter table Plant_Author add constraint FKA645061F79361E8B foreign key (Author_id) references Authors (Author_id);
alter table Plant_Author add constraint FKA645061FBBAC6574 foreign key (Plant_name_id) references vwMonocot_Name (Plant_name_id);
alter table Plant_Citation add constraint FK7DB31A7B4638E194 foreign key (Publication_edition_id) references Publication_Edition (Publication_edition_id);
alter table Plant_Citation add constraint FK7DB31A7BBBAC6574 foreign key (Plant_name_id) references vwMonocot_Name (Plant_name_id);
alter table Plant_Locality add constraint FK44FD0CF7BBAC6574 foreign key (Plant_name_id) references vwMonocot_Name (Plant_name_id);
alter table Publication_Edition add constraint FK68B0B26B51A05C29 foreign key (Publication_id) references Publication (Publication_id);
alter table vwMonocot_Name add constraint FK32FCC46339DBD09 foreign key (Climate_id) references Climate (Climate_id);
alter table vwMonocot_Name add constraint FK32FCC46285A4A5B foreign key (Place_of_publication_id) references Place_of_Publication (Place_of_publication_id);