$(function(){
    // user Regiter validation
    var $userRegister=$("#userRegister");
    $userRegister.validate({
        rules:{
            name:{
                required:true,
                lettersonly:true
            },
            email:{
                reuired:true,
                space:true,
                email:true
            },
            mobileNumber:{
                required:true,
                space:true,
                numericsonly:true,
                minlength:10,
                maxlength:12
            },
            password:{
                required:true,
                space:true
            },
            confirmPassword:{
                required:true,
                space:true,
                equalTo:"#password"

            },
            address:{
                required:true,
                all:true
            },
            city:{
                required:true,
                space:true
            },
            state:{
                required:true
            },
            pincode:{
                required:true,
                space:true,
                numericsonly:true
            }
            

        },
        messages:{
            name:{
                required:"Name Required",
                lettersonly:"Invalid Name (There should be no numbers in name)"
            },
            email:{
                required:'Email required',
                space:'Space not allowed',
                email:'Invalid Email'

            },
            mobileNumber:{
                required:'Mobile Number is required',
                space:'Space not allowed',
                numericsonly:'Invalid Mobile Number',
                minlength:'Minimum 10 digit number',
                maxlength:'Maximum 12 digit number'
            },
            password:{
                required:'Pasword is required',
                space:'Space not allowed'
            },
            confirmPassword:{
                required:'Confirm password required',
                space:'Space not allowed',
                equalTo:"Passwords do not match"

            },
            address:{
                required:'Address required',
                
            },
            city:{
                required:'City required',
                space:"Space not allowed"
            },
            state:{
                required:"State required"
            },
            pincode:{
                required:"Pincode required",
                space:'Space not allowed',
                numericsonly:'Only numerics allowed'
            },



        },
        errorPlacement: function (error, element) {
            // Append the error after the parent '.input-group' div
            error.insertAfter(element.closest('.input-group'));
        },

    })
})


//Reset Password validation
var $resetPassword=$("#resetPassword");
$resetPassword.validate({
    rules:{
        password:{
            required:true,
            space:true
        },
        confirmPassword:{
            required:true,
            space:true,
            equalTo:"#password"

        },
    },
    messages:{
        password:{
            required:'Pasword is required',
            space:'Space not allowed'
        },
        confirmPassword:{
            required:'Confirm password required',
            space:'Space not allowed',
            equalTo:"Passwords do not match"

        },

    },
    errorPlacement: function (error, element) {
        // Append the error after the parent '.input-group' div
        error.insertAfter(element.closest('.input-group'));
    },

})




jQuery.validator.addMethod('lettersonly',function(value,element){
    return /^(?!\s+$)[a-zA-Z,'. -]+$/.test(value);
});

jQuery.validator.addMethod('space',function(value,element){
    return /^[^-\s]+$/.test(value);
});

jQuery.validator.addMethod('all',function(value,element){
    return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
});

jQuery.validator.addMethod('numericsonly',function(value,element){
    return /^[0-9]+$/.test(value);
});

