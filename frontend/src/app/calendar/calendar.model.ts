export interface AvailableTime {
  fromHour: string;
  untilHour: string;
}

export type CalendarResponse = AvailableTime[];

export const LESSON_TIME = 60;

export const LESSON_TIMES = [45, 60, 90, 120];

export const TUTOR_ID = 1;
