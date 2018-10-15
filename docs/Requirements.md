#Requirements Digest (comments in grey boxes are my interpretations and assumptions)


## The Application must accept input from at most 5 concurrent clients on TCP/IP port 4000
    
        -TCP/IP protocol
        -Port 4000
        -Pool size = 5

## Input lines presented to the Application via its socket must either be composed of exactly nine decimal digits (e.g.: 314159265 or 007007009) immediately followed by a server-native newline sequence; or a termination sequence as detailed in #9, below.

        -Single digit numeric (0-9) 
        -Quantity of 9
        -What does system do if 10 are entered? <del>Ignore #10?  Respond with an error message?  Both?</del> 
        the connection is closed, input discarded, no feedback/notification to the client. 
        -/n == terminator


## Numbers presented to the Application must include leading zeros as necessary to ensure they are each 9 decimal digits.

    -System will sit in a WAIT mode until 9 are received.  
    -If less than 9 digits are entered followed by a terminating sequence, we will not prepend the 
    sequence with 0's; i.e., no lpad with 0 in order to create a 9 digit numeric.  <del>an error is returned.</del> 
    the connection is closed, input discarded, no feedback/notification to the client.
    
 
## The log file, to be named "numbers.log”, must be created anew and/or cleared when the Application starts.

     -Log name is numbers.log
     -<strong>Log is ephemeral and loses contents when application restarts. ***</strong>

   
## (#2)Only numbers may be written to the log file. Each number must be followed by a server-native newline sequence.

    -System will sit in a -When a character is included in a series of numeric values, 
    <del>an error is returned.</del> 
    the connection is closed, input discarded, 
    no feedback/notification to the client. 


## No duplicate numbers may be written to the log file.

    -When a duplicate number is entered, does this constitude "invalid line of input?".
   


## <strong>Any data that does not conform to a valid line of input should be discarded and the client connection terminated immediately and without comment.</strong>

## (#1) Every 10 seconds, the Application must print a report to standard output:
    
    - The difference since the last report of the count of new unique numbers that have been received.
    - Count current - count last time
    - The difference since the last report of the count of new duplicate numbers that have been received.
    -  Count current duplicates received - count last reported duplicates received
    - The total number of unique numbers received for this run of the Application.
    - This should be the same as Count current, above
    -  Example text for #8:
       `Received 50 unique numbers, 2 duplicates. Unique total: 567231`

## If any connected client writes a single line with only the word "terminate" followed by a server native newline sequence, the Application must disconnect all clients and perform a clean shutdown as quickly as possible.           
