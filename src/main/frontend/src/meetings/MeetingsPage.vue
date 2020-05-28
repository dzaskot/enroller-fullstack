<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length === 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: [],
                message: '',
                isError: false
            };
        },
        created () {
            // fetch the data when the view is created and the data is
            // already being observed
            this.fetchData()
        },
        watch: {
            // call again the method if the route changes
            '$route': 'fetchData'
        },
        methods: {
            fetchData () {
                this.$http.get(`meetings`)
                    .then(response => {
                        this.meetings = response.body
                    })
                    .catch();
            },
            addNewMeeting(meeting) {
                this.$http.post('meetings', meeting)
                    .then(response => {
                        this.fetchData();
                    })
                    .catch(() => this.failure('Dodawanie spotkania nieudane.'));
            },
            addMeetingParticipant(meeting) {
                //meeting.participants.push(this.username);
                this.$http.post('participants/' + this.username + "/" + meeting.id)
                    .then(() => {
                        this.fetchData();
                    })
                    .catch(response => this.failure('Błąd przy dodawaniu uczestnika. Kod odpowiedzi: ' + response.status));
            },
            removeMeetingParticipant(meeting) {
                meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
            },
            deleteMeeting(meeting) {
                this.$http.delete('meetings/' + meeting.id, this.meetings.indexOf(meeting))
                    .then(() => {
                        this.fetchData();
                    })
                    .catch(response => this.failure('Błąd przy usuwaniu spotkania. Kod odpowiedzi: ' + response.status));
            },
            success(message) {
                this.message = message;
                this.isError = false;
            },
            failure(message) {
                this.message = message;
                this.isError = true;
            }
        }
    }
</script>
