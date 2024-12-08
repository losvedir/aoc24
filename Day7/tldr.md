# Part 1

Gemini, Opus, and 4o all used `int` first, which caused an exception when
parsing the input.

When pasting in the exception, they all identified the issue and updated the
code to use `long` instaed, which worked.

Sonnet got it right immediately.

# Part 2

Opus, Sonnet, 4o all got it right.

Gemini returned an answer that was too low. I asked it to "double check the
logic"

> Explanation of the correction:

> The previous code was incorrectly multiplying or adding the currentValue with
> the concatenatedValue before making the recursive call. This was causing
> incorrect calculations in many cases.

> The corrected code now passes the concatenatedValue directly to the recursive
> call, allowing the next recursive step to handle the addition or
> multiplication with the accumulated value.

> I apologize again for the error in my previous response. With this correction,
> the code should now produce the accurate total calibration result for Part 2.

Still didn't work.
