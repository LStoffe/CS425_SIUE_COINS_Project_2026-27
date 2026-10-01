# BIG Application New Graphics Folder Documentation

## Overview
The `new_graphics/` folder contains the updated user interface assets for the BIG (Business Information Game) application. This folder represents a redesigned, more modern web interface compared to the original graphics, featuring improved navigation, styling, and user experience elements.

## Folder Structure

### **Main HTML Files:**
- **`index.html`** - Main application interface with navigation menu
- **`index_final.html`** - Final version of the main interface
- **`index1.html`** - Alternative version of the main interface
- **`login.html`** - User login page
- **`logout.html`** - User logout page
- **`intro.html`** - Application introduction page
- **`manual.html`** - User manual access page
- **`admin.html`** - Administrator interface

### **Styling and Assets:**
- **`big.css`** - Main stylesheet for the new interface
- **`images/`** - Complete set of graphical assets (78 files)
- **`js/`** - JavaScript libraries for navigation and interactivity (11 files)

## HTML Interface Files

### **Main Interface (`index.html`)**
- **Purpose**: Primary application interface with full navigation
- **Layout**: Table-based layout with image slices
- **Features**:
  - Logo and title graphics
  - Navigation menu with rollover effects
  - Main content area
  - Responsive design elements
- **Navigation Items**:
  - Logout, Inbox, Bidding, Project Management
  - Consulting, Financial Reports, Published Reports
  - Financial Services, Options, Help

### **Login Interface (`login.html`)**
- **Purpose**: User authentication page
- **Features**:
  - Clean, centered layout
  - Logo and title graphics
  - Login form integration
  - JavaScript preloading for smooth experience
- **JavaScript Integration**:
  - Image preloading functions
  - Menu system integration
  - Browser compatibility handling

### **Administrative Interface (`admin.html`)**
- **Purpose**: Administrator-specific interface
- **Features**: Administrative tools and controls

## CSS Styling (`big.css`)

### **Style Definitions:**
```css
body {
    font-family: Arial, Helvetica, sans-serif;
    color: #000000;
}

a:hover {
    color: #003399;
}

a:link {
    color: #0033CC;
}

a:visited {
    color: #000066;
}
```

### **Design Characteristics:**
- **Font**: Arial, Helvetica, sans-serif
- **Color Scheme**: Blue-based (#0033CC, #003399, #000066)
- **Link States**: Different colors for hover, link, and visited states
- **Clean Typography**: Professional, readable font choices

## Image Assets (`images/` directory)

### **Logo and Branding:**
- **`logo.gif`** - Main application logo
- **`logo_no_buddons.gif`** - Logo without navigation buttons
- **`lib_logo.gif`**, **`lib_logo-17.gif`** - Library branding
- **`const_logo.gif`** - Construction industry branding
- **`hswlogo.gif`** - Additional branding element

### **Navigation Graphics:**
- **Menu Buttons**: Rollover states for all navigation items
  - `bidding_up.gif`, `bidding_down.gif`
  - `financial_up.gif`, `financial_down.gif`
  - `project_up.gif`, `project_down.gif`
  - `consulting_up.gif`, `consulting_down.gif`
  - `help_up.gif`, `help_down.gif`
  - `options_up.gif`, `options_down.gif`
  - `logout_up.gif`, `logout_up.gif`

### **Title Graphics:**
- **Section Titles**: Dedicated title graphics for each section
  - `title_project.gif` - Project management
  - `title_login.gif`, `title_login2.gif` - Login pages
  - `title_bidding.gif` - Bidding system
  - `title_financialreports.gif` - Financial reports
  - `title_consultingservices.gif` - Consulting services
  - `title_help.gif` - Help system
  - `title_admin.gif` - Administration

### **Interface Elements:**
- **Backgrounds**: `background_off.gif`, `background_on.gif`
- **Spacers**: Various spacer graphics for layout
- **Bars**: `bluebar.gif`, `inbox_bar.gif`
- **Text Elements**: `text_game.gif`, `text_info.gif`
- **Welcome Elements**: `welcome_logo.gif`, `welcome_inbox.gif`

### **Splash Graphics:**
- **`big_splash_*.gif`** - Multiple splash screen graphics (01, 02, 03, 05, 06, 08, 11, 12, 13, 17)
- **Purpose**: Loading screens and introductory graphics

## JavaScript Libraries (`js/` directory)

### **Core JavaScript Files:**

#### **`sniffer.js`** - Browser Detection
- **Purpose**: Detects browser type and version
- **Features**:
  - Internet Explorer detection (IE4, IE5, IE6)
  - Netscape detection (NS4, NS6, NS7)
  - Mozilla/Firefox detection
  - Opera detection (OP5, OP6)
  - Konqueror detection
  - Platform detection (Windows, Mac, Linux)
- **Usage**: Enables browser-specific functionality

#### **`menu.js`** - Menu System Core
- **Purpose**: Core menu system functionality
- **Features**:
  - Dynamic menu generation
  - Browser-specific menu loading
  - Position calculation and alignment
  - Menu item management
- **Version**: UDM (Ultimate Dropdown Menu) v3.6.2

#### **Browser-Specific Menu Files:**
- **`menu_ie4.js`** - Internet Explorer 4 compatibility
- **`menu_ie5.js`** - Internet Explorer 5 compatibility
- **`menu_ns4.js`** - Netscape 4 compatibility
- **`menu_moz.js`** - Mozilla/Firefox compatibility
- **`menu_op5.js`** - Opera 5 compatibility
- **`menu_op6.js`** - Opera 6 compatibility

#### **Supporting JavaScript:**
- **`library.js`** - Core library functions
- **`library_main.js`** - Main library functions
- **`style.js`** - Style and formatting functions

### **Menu System Features:**
- **Multi-level Menus**: Support for main, sub, and child menus
- **Rollover Effects**: Dynamic menu item highlighting
- **Browser Compatibility**: Specific implementations for different browsers
- **Position Management**: Automatic positioning and alignment
- **Window Management**: Custom window opening functions

## Design Philosophy

### **User Experience Improvements:**
- **Visual Consistency**: Unified color scheme and typography
- **Navigation Clarity**: Clear menu structure and rollover effects
- **Professional Appearance**: Clean, business-appropriate design
- **Accessibility**: Standard HTML with proper alt tags and structure

### **Technical Implementation:**
- **Table-based Layout**: Consistent with web standards of the era
- **Image Slicing**: Optimized graphics for fast loading
- **Progressive Enhancement**: JavaScript enhances but doesn't require
- **Cross-browser Compatibility**: Extensive browser detection and support

## File Statistics

### **Content Summary:**
- **HTML Files**: 8 interface files
- **CSS Files**: 1 main stylesheet
- **Image Files**: 78 graphics (GIF format)
- **JavaScript Files**: 11 library files
- **Total Files**: 98 files

### **Image Categories:**
- **Navigation Graphics**: 20+ menu button states
- **Title Graphics**: 15+ section titles
- **Logo/Branding**: 8+ logo variations
- **Interface Elements**: 20+ UI components
- **Splash Graphics**: 10+ splash screen images
- **Layout Elements**: 15+ spacers and backgrounds

## Integration with Main Application

### **Build Process:**
- **Copy Operation**: Files copied to `build/dist/` during build
- **Web Integration**: Referenced by JSP pages and servlets
- **Static Resources**: Served directly by web server

### **Usage in Application:**
- **JSP Integration**: Referenced in JSP pages for styling and graphics
- **Menu System**: JavaScript menus integrated with Struts actions
- **Form Styling**: CSS applied to form elements and layouts
- **Navigation**: Menu system provides application navigation

## Browser Compatibility

### **Supported Browsers:**
- **Internet Explorer**: 4.0, 5.0, 6.0
- **Netscape**: 4.x, 6.x, 7.x
- **Mozilla/Firefox**: Early versions
- **Opera**: 5.x, 6.x
- **Konqueror**: 2.2+

### **Fallback Support:**
- **Graceful Degradation**: Works without JavaScript
- **Image Fallbacks**: Alt text for all images
- **CSS Fallbacks**: Basic styling without advanced features

## Notes
- The new_graphics folder represents a significant UI upgrade from the original interface
- All graphics are in GIF format, optimized for web delivery
- The JavaScript menu system provides sophisticated navigation capabilities
- The design maintains professional appearance suitable for educational/business use
- Browser detection ensures compatibility across different platforms and versions
- The interface is designed to work with the Struts-based backend application
- Image slicing and optimization techniques are used for fast loading
- The design follows web standards and accessibility guidelines of the era
