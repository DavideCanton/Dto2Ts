import { JsonArray, JsonClass, JsonProperty, SerializeFn } from 'at-json';
import { JsonDateISO } from 'creapp-common-lib';

@JsonClass()
export class ExampleDto {
    @JsonProperty()
    name: string;
    @JsonDateISO()
    dateOfBirth: Date | null;
    @JsonProperty()
    uid: string | null;
    @JsonArray()
    names: string[] = [];
    serialize: SerializeFn;
}

