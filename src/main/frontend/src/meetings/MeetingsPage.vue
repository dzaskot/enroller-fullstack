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
                meetings: []
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
                this.meetings.push(meeting);
            },
            addMeetingParticipant(meeting) {
                meeting.participants.push(this.username);
            },
            removeMeetingParticipant(meeting) {
                meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
            },
            deleteMeeting(meeting) {
                //this.meetings.splice(this.meetings.indexOf(meeting), 1);
                this.$http.delete('meetings/' + meeting.id, this.meetings.indexOf(meeting))
                    .then(() => {
                        this.fetchData();
                        this.success('Spotkanie zostało usunięte.');
                    })
                    .catch(response => this.failure('Błąd przy usuwaniu spotkania. Kod odpowiedzi: ' + response.status));
            }
        }
    }
</script>
