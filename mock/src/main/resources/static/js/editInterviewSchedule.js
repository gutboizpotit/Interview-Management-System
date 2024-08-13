$(document).ready(function() {
    $('#interviewers').select2({
        placeholder: "Select interviewer...",
        allowClear: true
    });
    $('#confirmCancelButton').on('click', function() {
        console.log("Confirm Cancel Button Clicked");
        $('#cancelScheduleForm').submit();
        $('#cancelConfirmationModal').modal('hide');
    });

    $('#assignMeBtn').on('click', function() {
        var username = $('#currentUsername').val();
        var fullName = $('#currentFullName').val();
        var userId = $('#currentUserId').val();
        var optionText = fullName + ' (' + username + ')';
        console.log(username)
        console.log(fullName);
        console.log(userId);

        if ($('#recruiter-owner option[value="' + userId + '"]').length === 0) {
            $('#recruiter-owner').append('<option value="' + userId + '">' + optionText + '</option>');
        }

        $('#recruiter-owner').val(userId).trigger('change');
    });

    $('#candidate-name').change(function() {
        var candidateId = $(this).val();
        if (candidateId) {
            $.ajax({
                url: '/interview-schedule/jobs-by-candidate',
                type: 'GET',
                data: {candidateId: candidateId},
                success: function(data) {
                    var jobSelect = $('#job');
                    jobSelect.empty();
                    jobSelect.append('<option value="" disabled selected>Select a job...</option>');
                    $.each(data, function(index, job) {
                        jobSelect.append('<option value="' + job.id + '">' + job.title + '</option>');
                    });
                }
            });
        } else {
            $('#job').empty();
            $('#job').append('<option value="" disabled selected>Select a job...</option>');
        }
    });
    $('#schedule-time-from').change(validateTime);
    $('#schedule-time-to').change(validateTime);

    var today = new Date().toISOString().split('T')[0];
    $('#schedule-date').attr('min', today);

    function validateTime() {
        var scheduleDate = $('#schedule-date').val();
        var timeStart = $('#schedule-time-from').val();
        var timeEnd = $('#schedule-time-to').val();
        var now = new Date();
        var selectedDate = new Date(scheduleDate + 'T' + timeStart);

        if (selectedDate < now) {
            $('#time-start-warning').text('Start time cannot be in the past.');
        } else {
            $('#time-start-warning').text('');
        }

        if (timeStart && timeEnd && timeStart >= timeEnd) {
            $('#time-end-warning').text('End time must be after start time.');
        } else {
            $('#time-end-warning').text('');
        }
    }
    $('#interviewScheduleForm').submit(function(event) {
        if ($('#time-start-warning').text() !== '' || $('#time-end-warning').text() !== '') {
            event.preventDefault();
        }
    });
    $('#schedule-title').change(validateTitle);
    $('#location').change(validateLocation);
    $('#meeting-id').change(validateMeetingId);

    function validateTitle(){
        var title = $('#schedule-title').val().trim();
        if (title.length === 0) {
            $('#title-warning').text('Title cannot be empty or just whitespace.');
        }
        else $('#title-warning').text('');
    }
    function validateLocation(){
        var location = $('#location').val().trim();
        if (location.length === 0) {
            $('#location-warning').text('Location cannot be empty or just whitespace.');
        }
        else $('#location-warning').text('');
    }
    function validateMeetingId(){
        var meetingId = $('#meeting-id').val().trim();
        if (meetingId.length === 0) {
            $('#meeting-id-warning').text('Meeting ID cannot be empty or just whitespace.');
        }
        else $('#meeting-id-warning').text('');
    }
});

document.addEventListener('DOMContentLoaded', function() {
    var toast = document.getElementById('toast');

    if (toast) {
        var message = document.getElementById('toast-content').textContent;

        console.log(message)

        if (message.includes("successfully!")) {
            toast.className = 'toast show success';
        } else if (message.includes("Fail")) {
            toast.className = 'toast show error';
        } else {
            toast.className = 'toast show';
        }

        toast.classList.add('show');
        setTimeout(function() {
            toast.classList.remove('show');
        }, 3000);
    }
});
