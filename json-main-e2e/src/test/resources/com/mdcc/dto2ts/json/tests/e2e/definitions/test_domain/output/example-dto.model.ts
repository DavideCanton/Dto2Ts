import { JsonClass, JsonProperty, SerializeFn } from 'at-json';
import { Domains, ILocalizableProperty, JsonLocalizableProperty } from 'creapp-common-lib';

@JsonClass()
export class ExampleDto {
    @JsonLocalizableProperty(Domains.AddressType)
    codaddresstype: ILocalizableProperty<string> | null;
    @JsonLocalizableProperty(Domains.ResidenceType)
    codresidencetype: ILocalizableProperty<string> | null;
    @JsonProperty()
    codstreettype: string;
    serialize: SerializeFn;
}

