# AppX_List
This is basically a template for displaying lists to the user in table format.

This is very important for events or for anything that requires us to have entries on multiple columns because it's easier to read and better formatted this way. Here I have created a database for the events in one of the files, which will hold all the entries for the events in it. I am still learning SQL for Android but my dad actually works and codes exclusively in SQL for his job so I have asked him for help and received some useful tips and suggestions on debugging and working with database entries (requesting the entries or sorting them by a particular order as necessary, or anything in between). It should work and function properly as needed however, please be aware that it has some minor error that I might have overlooked so for now do NOT use any of the calls to the database entries because it will cause the application to crash. I would also need to actually work on completing this code because it is mostly unfinished, but feel free to use this template for any lists you need to display multiple elements on one line as needed.

Some helpful tips on debugging that I had to figure out the hard way (and by myself):
-When modifying how the database functions, such as adding now columns to the database where none actually exist, if you attempt to access the database for whatever reason be it to fetch some data or add new entries to it, it will cause the application to crash on startup and no reason will be provided. The reason however is that the database cannot update its values in this way, even though you might write it in code and it compiles fine. What you will need to do in this case is update the database_version value to a higher one (upgrade) but I added a function thta also allows you to downgrade the version for whatever reason (I had one particularly embarassing dilemma in which I had to use this functionality for this reason) and this causes it to automatically run the onCreate() function which completely erases and wipes all information from the database currently and switches to a brand new template (but it's not really wiping the data that it currently has stored, you should be able to hypothetically get it back if you switch back to this version using the downgrade method that was just mentioned) so that you actually start from a clean slate and it actually modifies itself upon calling this onCreate() method so it takes into accuont new columns/rows which entirely changes the way that it is structured, and then the application will run fine and you can access it fine without any problems. But it's just something to keep in mind when working with databases, and suddenly in your code you decide to change the way a database is structured by having it store an additional value that gets passed to it, and find your application crashes for no reason and it can be very hard to go about debugging if you don't know the exact problem so -- just something to keep in mind.

