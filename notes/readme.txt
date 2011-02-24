HISTORY
-------

Version 0.7.3 :
	- BG 3190240: Bad request exception
	- BG 3188712: URI Too Large Exception on Manual Sync
	- BG 3186886: Improve error message for tasks with no title
	- FR 3187796: keep current search list after sync
	- FR 3179777: More forgiving time parsing
	- Bug fixed: background of the search list not always displayed
	- Improved UI (especially for Mac)
	- Improved startup scripts
	- Improved error handling
	- Use jcalendar instead of microba
	- Improved date/time chooser (start date and due date)

Version 0.7.2 : 18/02/2011
	- BG 3178988: encoding problem
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

TaskUnifier: Manage your tasks and synchronize them
---------------------------------------------------
Copyright (C) 2010  Benjamin Leclerc

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
