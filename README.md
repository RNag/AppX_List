# AppX_List
This is basically a template for displaying lists to the user in table format.

This is very important for events or for anything that requires us to have entries on multiple columns because it's easier to read and better formatted this way. Here I have created a database for the events in one of the files, which will hold all the entries for the events in it. I am still learning SQL for Android but my dad actually works and codes exclusively in SQL for his job so I have asked him for help and received some useful tips and suggestions on debugging and working with database entries (requesting the entries or sorting them by a particular order as necessary, or anything in between). Please note that calls to the database now work as intended.

EDIT (10/4/15):The issue with accessing the database has been addressed. There was a minor problem, it has been fixed. Also, some subtle changes and added features have been implemented. This includes being able to sort columns in ascending or descending order, the 'new entry' button now works as intended and takes all input field values into consider, and also fixed some problems with the table layout. There is one unresolved issue, and really this is not unresolved but something neglected for now because I didn't have enough time (it will be added soon). This is that 'delete' entry only removes it from current list displayed to the user, not from the actual location where it is stored in memory unfortunately. But this will be fixed real soon, it's a very simple solution and just a matter of adding a separate function in the other .class file and then referencing it. Now let us discuss another issue (not really an issue, but a problem I think I will pose to you).

 I found it hard to create a separate table (after the one that currently holds all the entries displayed to the user) immediately preceding the one that exists in the interface screen, used to hold form elements where the user can enter a new entry to be added to the running list. Perhaps this is because of how I instantiated or defined the parameters in the Java code which was basically copy-pasted (or transliterated if you like) from XML layout code. But then again, perhaps not. I am just thinking it's the most likely culprit for now. The reasoning is I have defined the style of this main table as something like "MATCH_PARENT" for width/height of the table layout, and I'm placing more than one table inside this layout and it might be sure what to make of it. I don't really know what the problem is, but whatever the reason is I can't even get it to show up on the XML Preview screen when I add a new table with a button instantiated in it and see if its displayed or not. Actually it creates a table but it's somewhere on the far right of the screen (far from the visible view on the screen anyway). So rather than try to play around with XML and then try to get it to work with Java code as well (because I know how exhaustive that worked out to be the last time I attempted to do something like that), for now a 'quick fix' will be that upon creation of a new entry to the running list, it'll just collapse all the elements currently contained in the main table (or just clear all views from it, in 'Java code' jargon) and then instantiate all previously held elements back into the table (or repopulate all known elements, which is what I named my function that deals with these exact same behavior) which results in, seeing as it invariably ends up fetching all the entries held in database in order to accomplish this, all entries being displayed in addition to the new one that was currently added to the running list. So that works well for now, but if anyone finds a better way to do this in code (i.e. inserting a row somewhere between sequential rows in a dynamic table, which I haven't figured out if it's possible to do so with Java) then please let me know. Also, feel free to post any changes or additional features you would like to see implemented. 


--
Some helpful tips on debugging that I had to figure out the hard way (and by myself):
-When modifying how the database functions, such as adding now columns to the database where none actually exist, if you attempt to access the database for whatever reason be it to fetch some data or add new entries to it, it will cause the application to crash on startup and no reason will be provided. The reason however is that the database cannot update its values in this way, even though you might write it in code and it compiles fine. What you will need to do in this case is update the database_version value to a higher one (upgrade) but I added a function thta also allows you to downgrade the version for whatever reason (I had one particularly embarassing dilemma in which I had to use this functionality for this reason) and this causes it to automatically run the onCreate() function which completely erases and wipes all information from the database currently and switches to a brand new template (but it's not really wiping the data that it currently has stored, you should be able to hypothetically get it back if you switch back to this version using the downgrade method that was just mentioned) so that you actually start from a clean slate and it actually modifies itself upon calling this onCreate() method so it takes into accuont new columns/rows which entirely changes the way that it is structured, and then the application will run fine and you can access it fine without any problems. But it's just something to keep in mind when working with databases, and suddenly in your code you decide to change the way a database is structured by having it store an additional value that gets passed to it, and find your application crashes for no reason and it can be very hard to go about debugging if you don't know the exact problem so -- just something to keep in mind.

