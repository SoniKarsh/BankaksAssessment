# BankaksAssessment

## First Screen
This screen has a spinner or dropdown which has 3 options and each mapped to int 1, 2, 3. On selection of each we need to call an API which will return the
details of the next screen.

## Second Screen
This will be dependent on the option selected from the first screen. Based on the response from the first screen render the UI dynamically.

![Screenshot](https://github.com/SoniKarsh/BankaksAssessment/blob/master/screenshot/ss.png)

## Details of the project
In the second screen needed to get the response and there were basically UI types given based on which you need to render different widgets like for
Dropdown type go for spinner and for textfield go for EditText and then you will have params from response like isMandatory field which is used for whether
this field should be filled compulsory or optional and then there will be params like Placeholder, Hint, Name, etc.

Regex will be given in order to validate data based on regex. so, if regex doesn't match then throw an error. Data types are given to make forms input type 
for ex-> if data type is string then we can show entire keyboard and if it is int then only need to show number type keyboard.

Handling of error cases based on above mentioned params when clicked on validate button.

### Note
There are two issues that i have found and both are related to Regex and i have also sent a mail regarding the same.

