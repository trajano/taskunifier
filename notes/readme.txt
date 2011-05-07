HISTORY
-------

Version 0.8.8
	- Memory leaks
	- Bug fixed: Repeat with completion date
	- Bug fixed: Select empty value in template fields
	- Bug fixed: Order of tasks with the same title
	- Bug fixed: Combobox search with multiple letters
	- FR 3287354: Priority icons
	- FR 3181023: Editable general searchers
	- FR 3207539: Colors by importance
	- BF 3241160: Task edit window size
	- Change searcher sort order via drag and drop

Version 0.8.7
	- Offline repeat
	- Repeat help
	- Default repeat values in task edit window and template window
	- Bug fixed: Edit task note with edit window
	- Check plugin version
	- FR 3274185: Add subtask button
	- FR 3208904: Subtask will take over contexts, folders, goals, locations from parent
	- BSD Licence

Version 0.8.6
	- Shortcut CTRL+E to edit task
	- Define start time and due time in template
	- Menu when right click on a task 
	- FR 3207558: Double click on context, folder... to edit it
	- FR 3276583: Default sort order
	- FR 3207557: Default repeat values
	- Bug fixed: Remove color of folder/goal/location
	- Delete tasks with "Delete" key

Version 0.8.5
	- FR 3266522: Narrower task row
	- BF 3207477: Data entry by keyboard in length field not possible
	- BF 3263325: Focus lost after sync
	- BF 3263920: Carriage return in task note
	- Bug fixed: Java out of heap space
	- Keep completed tasks: set max to 9999
	- Show the history of the plugins

Version 0.8.4
	- FR 3252640: No date value in searcher
	- BF 3241151: Close edit task window doesn't delete the task
	- Improve task note
	- Plugins are now stored in the data folder
	- Bug fixed: error when installing plugin

Version 0.8.3
	- FR 3235434: Option for HTML text formatting in task notes
	- FR 3208907: Hyper links in task notes
	- FR 3222956: Smaller date deletion button

Version 0.8.2: 23/03/2011
	- FR 3213145: Combobox for reminder value
	- FR 3207413: Cancel button for task edit window
	- FR 3207446: Larger note text box
	- BF 3214534: Ubuntu crash at start
	- BF 3213050: Task length field in task edit window
	- Bug fixed: Select title & Edit task window with template
	- Creation of a plugin manager
	- Synchronization needs a license

Version 0.8.1
	- BF 3208322: Subtasks problem
	- FR 3207436: Smaller icons in settings

Version 0.8.0
	- FR 3194872: Combobox search
	- FR 3192554: German GUI
	- FR 3195235: Form to add/edit tasks
	- FR 3197261: Color for folders
	- FR 3196221: Save settings
	- FR 3194775: Sorting by importance
	- FR 3195225: Smaller top button bar
	- FR 3195256: Date format dd.mm.yyyy
	- FR 3194871: When adding a task, edit title
	- FR 3196009: Add new tasks always is done in the "all" folder
	- FR 3201299: Hide completed tasks in General ->All Tasks
	- BF 3195252: Date deleted on click
	- Bug fixed: Personal search: parent condition
	- Added jGoodies themes

Version 0.7.4
	- Bug fixed: Performance issue !!! 
	- Bug fixed: Invalid key !!!
	- FR 3195031: Popup task text
	- FR 3192919: Background synchronization (for scheduled sync)
	- FR 3192920: Setting synchronize on start
	- FR 3192920: Setting synchronize on close

Version 0.7.3
	- BF 3190245: Proxy Access Denied
	- BF 3190240: Bad request exception
	- BF 3188712: URI Too Large Exception on Manual Sync
	- BF 3186886: Improve error message for tasks with no title
	- FR 3187796: keep current search list after sync
	- FR 3179777: More forgiving time parsing
	- FR 3192241: Import/Export data
	- Bug fixed: background of the search list not always displayed
	- Improved UI (especially for Mac)
	- Improved startup scripts
	- Improved error handling
	- Use jcalendar instead of microba
	- Improved date/time chooser (start date and due date)

Version 0.7.2: 18/02/2011
	- BF 3178988: encoding problem
	- Bug fixed: Toodledo account creation
	- Bug fixed: task note not correctly saved on focus lost
	- Bug fixed: show new version message only once
	- Bug fixed: background of the search list not always displayed
	- Bug fixed: searcher condition on not already synced models
	- Bug fixed: searcher list count badges sometimes disappeared
	- Use system proxy setting has been removed
	- Windows installer: uninstall from control panel

Version 0.7.1
	- Added log bug and log feature request buttons
	- Improved handling of http errors
	- Task note is saved before sync (was not always the case)
	- Improved UI (especially for Mac)
	- Fixed bug with searcher conditions Completed and Star
	- Task note line wrap
	- Repeat field is now editable
	- Bug fixed: add/remove column with right click is not saved
	- Bug fixed: contributes to goal sometimes fails
	- Bug fixed: synchronization error

Version 0.7.0
	- Drag and drop multiple tasks at once
	- Delete multiple tasks at once
	- TaskUnifier installer for Windows
	- Batch add task
	- Added quick search field
	- Bug fixed: model references bug
	- Welcome screen (first execution)
	- Use system proxy settings option
	- Expand searcher tree when new searcher is added
	- Improved error handling
	- Open searcher edit when new searcher is added
	- Import/export searchers
	- Bug fixed: number condition in searchers didn't work
	- Added option to show/hide/sort completed tasks
	- Copy paste searchers
	- Double click on searcher opens the edit searcher windows
	- Plugin support in order to add new APIs
	- Add templates feature
	- Import/export templates
	- New help files
	- All the lists are now sorted

Version 0.6.4
	- Bug fixed: only first sorted column worked
	- Bug fixed: save scheduler delay error
	- When a task is added, "All Tasks" searcher is selected
	- When a task is added, it is selected
	- Added tooltips on searchers

Version 0.6.3
	- Added scheduled synchronization

Version 0.6.2
	- Bug fixed: proxy not removed after version check
	- Bug fixed: undo/redo wasn't working anymore
	- Bug fixed: synchronize window freeze when email/pwd missing
	- Bug fixed: personal search creation bug
	- Bug fixed: keyboard shortcuts on Mac OS
	- Bug fixed: subtask drag and drop bug on Mac OS
	- Error window: create report button
	- Default button defined in each dialog
	- Added searcher list background color in settings
	
Version 0.6.1
	- Improved Mac OS version
	- Date/Time format settings
	- Searchers displayed as a tree

Version 0.6
	- Toodledo API 2.0
	- GUI for personal searcher edition
	- Added JTattoo themes
	- Remove Substance themes (due to some issues)
	- Added updates check
	- Added help files

Version 0.5.2
	- Add create account button
	- Fix bug with Toodledo password

Version 0.5.1
	- Add error message when Toodledo account info are empty

Version 0.5
	- Initial release


LICENCE
-------

TaskUnifier
---------------------------------------------------
Copyright (c) 2011, Benjamin Leclerc
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

  - Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

  - Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

  - Neither the name of TaskUnifier or the names of its
    contributors may be used to endorse or promote products derived
    from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

JCalendar
---------
http://www.toedter.com/en/jcalendar/

Icons: Creative Commons
-----------------------
Licence: http://creativecommons.org/licenses/by-nd/3.0/

Mac Widgets
-----------
http://code.google.com/p/macwidgets/
Licence: http://www.gnu.org/licenses/lgpl.html

Themes: Substance
-----------------
Copyright (c) 2005-2010 Substance, Kirill Grouchnikov.
All Rights Reserved.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Themes: JTattoo
---------------
Copyright (c) 2002-2007 by MH Software-Entwicklung. All Rights Reserved.
MH Software-Entwicklung grants registered users ("Licensee") a non-exclusive, 
royalty free, license to use and redistribute this software in binary code form.
This software is provided "AS IS," without a warranty of any kind.
ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY 
IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR 
NON-INFRINGEMENT, ARE HEREBY EXCLUDED. MH SOFTWARE-ENTWICKLUNG SHALL NOT BE LIABLE 
FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING 
THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL MH SOFTWARE-ENTWICKLUNG BE LIABLE 
FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF 
LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF MH 
SOFTWARE-ENTWICKLUNG HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.

This software is not designed or intended for use in on-line control of aircraft, 
air traffic, aircraft navigation or aircraft communications; or in the design, 
construction, operation or maintenance of any nuclear facility. Licensee represents 
and warrants that it will not use or redistribute the Software for such purposes.
